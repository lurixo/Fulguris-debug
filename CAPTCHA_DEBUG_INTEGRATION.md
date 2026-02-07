# Captcha Debug Logger - Integration Guide

## 概述

这个调试日志工具会记录所有可能影响 Google 人机验证的信号，帮助你对比 Fulguris 和你自己浏览器的差异。

## 日志文件位置

- 主日志: `/storage/emulated/0/Android/data/<包名>/files/captcha_debug.log`
- 请求详情日志: `/storage/emulated/0/Android/data/<包名>/files/captcha_requests.log`

## 新增文件

```
app/src/main/java/fulguris/debug/CaptchaDebugLogger.kt
```

## 需要修改的文件 (共5处)

---

### 1. Application 类中初始化 (例如 `App.kt` 或其他 Application 子类)

在 `onCreate()` 中加入：

```kotlin
import fulguris.debug.CaptchaDebugLogger

// 在 onCreate() 中：
CaptchaDebugLogger.init(this)
```

---

### 2. `WebPageTab.kt` - 记录 WebView 设置和请求头

**文件:** `app/src/main/java/fulguris/view/WebPageTab.kt`

#### 2a. 在 `createWebView()` 方法末尾 (line ~535, `initializePreferences()` 之后) 添加：

```kotlin
import fulguris.debug.CaptchaDebugLogger

// 在 initializePreferences() 之后加入：
webView?.let { wv ->
    CaptchaDebugLogger.logWebViewSettings(wv)
    CaptchaDebugLogger.logRequestHeaders(requestHeaders)
}
```

#### 2b. 在 `initializePreferences()` 方法末尾 (line ~710, `applyDarkMode()` 之后) 添加：

```kotlin
// 记录偏好设置变更后的 headers 状态
CaptchaDebugLogger.logRequestHeaders(requestHeaders)
```

---

### 3. `WebPageClient.kt` - 记录请求/响应/阻断/页面事件

**文件:** `app/src/main/java/fulguris/view/WebPageClient.kt`

#### 3a. 在 `shouldInterceptRequest()` 中 (line ~201 开始的方法)

在现有的日志行之后添加调试日志：

```kotlin
override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
    Timber.v("$ihs : shouldInterceptRequest ...")

    // === 添加 Debug 日志 ===
    CaptchaDebugLogger.logRequest(request, request.isForMainFrame)

    val response = adBlock.shouldBlock(request, currentUrl)
    val wasBlocked = response != null
    val url = request.url.toString()

    // === 记录被屏蔽的请求 ===
    if (wasBlocked) {
        CaptchaDebugLogger.logBlockedRequest(url, request.isForMainFrame)
    }

    // ... 现有代码 ...

    // === 记录 NetworkEngine 处理的请求 ===
    val engine = networkEngineManager.getCurrentEngine()
    if (engine != null) {
        val engineResponse = engine.handleRequest(request)
        if (engineResponse != null) {
            CaptchaDebugLogger.logNetworkEngineRequest(url, engine.displayName)
            CaptchaDebugLogger.logResponse(url, engineResponse)
            return engineResponse
        }
    }

    return null
}
```

#### 3b. 在 `onPageStarted()` 中 (line ~455)

```kotlin
override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
    Timber.i("$ihs : onPageStarted - $url")

    // === 添加 Debug 日志 ===
    CaptchaDebugLogger.logPageEvent("onPageStarted", url)

    // ... 现有代码 ...
}
```

#### 3c. 在 `onPageFinished()` 中 (line ~360)

在 `onPageFinishedDone = true` 之后添加：

```kotlin
// Flag that we have called onPageFinished
onPageFinishedDone = true

// === 添加 Debug 日志 ===
CaptchaDebugLogger.logPageEvent("onPageFinished", url,
    "LoadTime: ${pageLoadDuration}ms, Resources: $iResourceCount")
CaptchaDebugLogger.logCookies(url)

// 对 Google 页面注入指纹检测 JS
val isGoogle = url.contains("google.com") || url.contains("recaptcha")
        || url.contains("gstatic.com") || url.contains("accounts.google")
if (isGoogle) {
    CaptchaDebugLogger.dumpFullSnapshot(view, webPageTab.requestHeaders, url)
    CaptchaDebugLogger.injectFingerprintCheck(view)
}
```

---

### 4. `WebPageChromeClient.kt` - 捕获 JS 控制台消息

**文件:** `app/src/main/java/fulguris/view/WebPageChromeClient.kt`

在 `onConsoleMessage()` 方法中，现有代码 `webPageTab.addConsoleMessage(consoleMessage)` 之后添加：

```kotlin
// === 添加 Debug 日志 ===
CaptchaDebugLogger.logConsoleMessage(
    messageLevel()?.name ?: "UNKNOWN",
    message(),
    lineNumber(),
    sourceId()
)
```

---

### 5. `UserPreferencesExtensions.kt` - 记录 UA 生成过程

**文件:** `app/src/main/java/fulguris/settings/preferences/UserPreferencesExtensions.kt`

在 `userAgent()` 函数的 `USER_AGENT_DEFAULT` 分支中添加日志：

```kotlin
USER_AGENT_DEFAULT -> {
    var userAgent = WebSettings.getDefaultUserAgent(application)
    CaptchaDebugLogger.log("UA Transform - Original: $userAgent")

    userAgent = Regex(" Build/.+; wv").replace(userAgent, "")
    CaptchaDebugLogger.log("UA Transform - After remove Build/wv: $userAgent")

    userAgent = Regex("Version/.+? ").replace(userAgent, "")
    CaptchaDebugLogger.log("UA Transform - After remove Version: $userAgent")

    userAgent = Regex("Android ([^;)]+);[^)]*\\)").replace(userAgent, "Android 10; K)")
    CaptchaDebugLogger.log("UA Transform - After Android 10 K: $userAgent")

    userAgent = Regex("Chrome/(\\d+)\\.\\d+\\.\\d+\\.\\d+").replace(userAgent, "Chrome/$1.0.0.0")
    CaptchaDebugLogger.log("UA Transform - Final: $userAgent")

    userAgent
}
```

注意：这里需要将 CaptchaDebugLogger 的 `log` 方法改为 `public`，或者添加一个 public wrapper。
我在 CaptchaDebugLogger 中已提供了 `logPageEvent` 等公开方法，
你也可以直接用 `Timber.d("CaptchaDebug: UA Transform - ...")` 替代，效果相同。

---

## 关键分析点

当你看日志时，重点关注以下差异（你的浏览器 vs Fulguris）：

### 最可能的原因（按优先级排序）：

1. **X-Requested-With 头**
   - 标准 WebView 会自动发送 `X-Requested-With: 你的包名`
   - Fulguris 将其设为空字符串来隐藏身份
   - **这是最可能导致你无法通过验证的原因**

2. **User-Agent 中的 WebView 标记**
   - `wv` 标记：标准 WebView UA 包含 `; wv)`
   - `Build/` 标记：暴露设备编译信息
   - `Version/4.0`：WebView 特有标记
   - Fulguris 全部移除了这些标记

3. **Client Hints (Sec-CH-UA-*)**
   - Android 13+ 的 WebView 会通过 Client Hints 暴露信息
   - Fulguris 修改了 Client Hints 使其与简化的 UA 一致

4. **navigator.webdriver**
   - 某些自动化框架会将其设为 `true`
   - 正常 WebView 应为 `false`

5. **广告拦截器**
   - 如果屏蔽了 `gstatic.com` 或 `recaptcha` 相关资源
   - 验证脚本会无法加载

6. **JavaScript 执行环境**
   - `window.chrome` 对象是否存在
   - `navigator.plugins` 是否为空
   - Canvas/WebGL 指纹

## 如何在你的浏览器中修复

基于以上分析，优先尝试：

```kotlin
// 1. 移除 X-Requested-With 头（最重要！）
webView.settings.apply {
    // ... 其他设置
}
// 方法 A: 在 shouldInterceptRequest 中移除
// 方法 B: 用以下代码设置空头
val headers = mapOf("X-Requested-With" to "", "X-Wap-Profile" to "")
webView.loadUrl(url, headers)

// 2. 清理 UA 字符串
var ua = WebSettings.getDefaultUserAgent(context)
ua = ua.replace(Regex(" Build/.+; wv"), "")
ua = ua.replace(Regex("Version/.+? "), "")
webView.settings.userAgentString = ua

// 3. 如果是 Android 13+，同步修改 Client Hints
// 参考 UserPreferencesExtensions.kt 中的 setReducedClientHints()
```
