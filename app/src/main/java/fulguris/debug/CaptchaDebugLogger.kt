/*
 * Debug logger for analyzing how Google's human verification (reCAPTCHA) works.
 * Logs all WebView signals that Google may use to distinguish a "real browser" from a WebView.
 *
 * Log file location: /storage/emulated/0/Android/data/<packageName>/files/captcha_debug.log
 *
 * Key factors that affect verification:
 * 1. User-Agent string (presence of "wv", "Build/", "Version/" markers)
 * 2. Client Hints (Sec-CH-UA-* headers)
 * 3. X-Requested-With header (exposes package name in standard WebView)
 * 4. X-Wap-Profile header (exposes device info)
 * 5. WebView settings (JavaScript, DOM Storage, cookies, etc.)
 * 6. Request/Response headers
 * 7. JavaScript environment signals (navigator.webdriver, etc.)
 * 8. Cookie handling
 */

package fulguris.debug

import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Singleton debug logger that writes to external files directory.
 * Thread-safe, auto-creates log file, rotates when > 5MB.
 */
object CaptchaDebugLogger {

    private const val TAG = "CaptchaDebug"
    private const val LOG_FILE_NAME = "captcha_debug.log"
    private const val DETAIL_LOG_FILE_NAME = "captcha_requests.log"
    private const val MAX_LOG_SIZE = 5 * 1024 * 1024L // 5MB

    private var logFile: File? = null
    private var detailLogFile: File? = null
    private var initialized = AtomicBoolean(false)
    private var enabled = AtomicBoolean(true)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    // Google-related URL patterns to focus on
    private val GOOGLE_PATTERNS = listOf(
        "google.com",
        "gstatic.com",
        "googleapis.com",
        "recaptcha",
        "accounts.google",
        "sorry/index",        // Google's CAPTCHA page
        "ServiceLogin",
        "challenge",
        "batchexecute",
    )

    /**
     * Initialize with the application context. Must be called once (e.g. in Application.onCreate).
     */
    fun init(context: Context) {
        if (initialized.getAndSet(true)) return

        try {
            // /storage/emulated/0/Android/data/<packageName>/files/
            val dir = context.getExternalFilesDir(null) ?: run {
                Timber.e("$TAG: getExternalFilesDir returned null")
                return
            }
            if (!dir.exists()) dir.mkdirs()

            logFile = File(dir, LOG_FILE_NAME)
            detailLogFile = File(dir, DETAIL_LOG_FILE_NAME)

            // Rotate if needed
            rotateIfNeeded(logFile!!)
            rotateIfNeeded(detailLogFile!!)

            log("=".repeat(80))
            log("CaptchaDebugLogger initialized")
            log("Package: ${context.packageName}")
            log("Log path: ${logFile!!.absolutePath}")
            log("Detail log path: ${detailLogFile!!.absolutePath}")
            log("Device: ${Build.MANUFACTURER} ${Build.MODEL}")
            log("Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            log("Time: ${dateFormat.format(Date())}")
            log("=".repeat(80))

        } catch (e: Exception) {
            Timber.e(e, "$TAG: Failed to initialize")
        }
    }

    /**
     * Log the complete WebView settings configuration.
     * Call this after WebView is created and configured.
     */
    fun logWebViewSettings(webView: WebView) {
        if (!enabled.get()) return
        try {
            val settings = webView.settings
            log("")
            log(">>> WebView Settings Dump <<<")
            log("  userAgentString: ${settings.userAgentString}")
            log("  defaultUserAgent: ${WebSettings.getDefaultUserAgent(webView.context)}")
            log("")
            log("  -- UA Comparison --")
            val defaultUA = WebSettings.getDefaultUserAgent(webView.context)
            val currentUA = settings.userAgentString
            log("  Has 'wv' marker (WebView): ${defaultUA.contains("; wv)")}")
            log("  Current UA has 'wv': ${currentUA.contains("; wv)")}")
            log("  Has 'Build/' marker: ${defaultUA.contains("Build/")}")
            log("  Current UA has 'Build/': ${currentUA.contains("Build/")}")
            log("  Has 'Version/' marker: ${defaultUA.contains("Version/")}")
            log("  Current UA has 'Version/': ${currentUA.contains("Version/")}")
            log("")
            log("  javaScriptEnabled: ${settings.javaScriptEnabled}")
            log("  domStorageEnabled: ${settings.domStorageEnabled}")
            log("  databaseEnabled: ${settings.databaseEnabled}")
            log("  javaScriptCanOpenWindowsAutomatically: ${settings.javaScriptCanOpenWindowsAutomatically}")
            log("  mediaPlaybackRequiresUserGesture: ${settings.mediaPlaybackRequiresUserGesture}")
            log("  mixedContentMode: ${mixedContentModeString(settings.mixedContentMode)}")
            log("  cacheMode: ${cacheModeString(settings.cacheMode)}")
            log("  allowContentAccess: ${settings.allowContentAccess}")
            log("  allowFileAccess: ${settings.allowFileAccess}")
            log("  allowFileAccessFromFileURLs: ${settings.allowFileAccessFromFileURLs}")
            log("  allowUniversalAccessFromFileURLs: ${settings.allowUniversalAccessFromFileURLs}")
            log("  useWideViewPort: ${settings.useWideViewPort}")
            log("  loadWithOverviewMode: ${settings.loadWithOverviewMode}")
            log("  builtInZoomControls: ${settings.builtInZoomControls}")
            log("  displayZoomControls: ${settings.displayZoomControls}")
            log("  textZoom: ${settings.textZoom}")
            log("  defaultTextEncodingName: ${settings.defaultTextEncodingName}")
            log("  saveFormData: ${settings.saveFormData}")
            log("")

            // Check CookieManager
            val cookieManager = CookieManager.getInstance()
            log("  -- Cookie Settings --")
            log("  acceptCookie: ${cookieManager.acceptCookie()}")
            log("  acceptThirdPartyCookies: ${cookieManager.acceptThirdPartyCookies(webView)}")
            log("")

            // Check Client Hints support
            logClientHints(settings)

            // Check WebView features
            logWebViewFeatures()

            // Important for autofill
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("  importantForAutofill: ${webView.importantForAutofill}")
            }

            log("<<< End WebView Settings >>>")
            log("")

        } catch (e: Exception) {
            Timber.e(e, "$TAG: Error logging WebView settings")
            log("ERROR logging WebView settings: ${e.message}")
        }
    }

    /**
     * Log Client Hints metadata (Sec-CH-UA-* headers).
     * These are critical - Google uses them to verify browser identity.
     */
    private fun logClientHints(settings: WebSettings) {
        log("  -- Client Hints (Sec-CH-UA) --")
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                androidx.webkit.WebViewFeature.isFeatureSupported(
                    androidx.webkit.WebViewFeature.USER_AGENT_METADATA
                )
            ) {
                val metadata = androidx.webkit.WebSettingsCompat.getUserAgentMetadata(settings)
                log("  Supported: YES")
                log("  Platform: ${metadata.platform}")
                log("  PlatformVersion: ${metadata.platformVersion}")
                log("  FullVersion: ${metadata.fullVersion}")
                log("  Model: ${metadata.model}")
                log("  Mobile: ${metadata.isMobile}")
                log("  Architecture: ${metadata.architecture}")
                log("  Bitness: ${metadata.bitness}")
                log("  Wow64: ${metadata.isWow64}")
                log("  BrandVersionList:")
                metadata.brandVersionList.forEach { brand ->
                    log("    - Brand: ${brand.brand}, Major: ${brand.majorVersion}, Full: ${brand.fullVersion}")
                }
            } else {
                log("  Supported: NO (API ${Build.VERSION.SDK_INT}, need >=33 + WebView support)")
            }
        } catch (e: Exception) {
            log("  Error reading Client Hints: ${e.message}")
        }
    }

    /**
     * Log supported WebView features.
     */
    private fun logWebViewFeatures() {
        log("  -- WebView Features --")
        val features = listOf(
            "FORCE_DARK",
            "FORCE_DARK_STRATEGY",
            "ALGORITHMIC_DARKENING",
            "USER_AGENT_METADATA",
            "SAFE_BROWSING_ENABLE",
            "SERVICE_WORKER_BASIC_USAGE",
            "WEB_MESSAGE_PORT_POST_MESSAGE_WITH_TRANSFERABLE",
        )
        for (featureName in features) {
            try {
                val field = androidx.webkit.WebViewFeature::class.java.getField(featureName)
                val value = field.get(null) as String
                val supported = androidx.webkit.WebViewFeature.isFeatureSupported(value)
                log("  $featureName: $supported")
            } catch (e: Exception) {
                log("  $featureName: unknown")
            }
        }
    }

    /**
     * Log the extra request headers that Fulguris adds.
     * The X-Requested-With header is KEY - standard WebView sets this to the package name.
     */
    fun logRequestHeaders(headers: Map<String, String>) {
        if (!enabled.get()) return
        log("")
        log(">>> Custom Request Headers (added by Fulguris) <<<")
        if (headers.isEmpty()) {
            log("  (none - X-Requested-With will default to package name!)")
            log("  *** WARNING: Without removing X-Requested-With, Google can identify this as a WebView ***")
        } else {
            headers.forEach { (key, value) ->
                val note = when {
                    key.equals("X-Requested-With", ignoreCase = true) && value.isEmpty() ->
                        " ← CRITICAL: Set to empty to hide package name from Google"
                    key.equals("X-Requested-With", ignoreCase = true) ->
                        " ← WARNING: Non-empty value exposes app identity"
                    key.equals("X-Wap-Profile", ignoreCase = true) && value.isEmpty() ->
                        " ← Set to empty to hide device info"
                    key.equals("DNT", ignoreCase = true) ->
                        " ← Do Not Track"
                    key.equals("Save-Data", ignoreCase = true) ->
                        " ← Save Data mode"
                    else -> ""
                }
                log("  $key: '$value'$note")
            }
            if (!headers.containsKey("X-Requested-With")) {
                log("  *** X-Requested-With NOT in custom headers → WebView default will send package name ***")
            }
        }
        log("<<< End Custom Request Headers >>>")
        log("")
    }

    /**
     * Log every HTTP request, with extra detail for Google-related requests.
     * This goes to the detail log file to avoid flooding the main log.
     */
    fun logRequest(request: WebResourceRequest, isMainFrame: Boolean) {
        if (!enabled.get()) return
        val url = request.url.toString()
        val isGoogleRelated = GOOGLE_PATTERNS.any { url.contains(it, ignoreCase = true) }

        // Always log Google-related requests to main log
        if (isGoogleRelated) {
            log("[REQ] ${if (isMainFrame) "MAIN" else "SUB "} ${request.method} $url")
            log("  Request headers from WebView:")
            request.requestHeaders?.forEach { (key, value) ->
                val masked = if (key.equals("cookie", ignoreCase = true) && value.length > 100) {
                    "${value.take(100)}... (${value.length} chars)"
                } else {
                    value
                }
                val flag = when {
                    key.equals("User-Agent", ignoreCase = true) -> " ← UA"
                    key.equals("X-Requested-With", ignoreCase = true) -> " ← *** IDENTITY HEADER ***"
                    key.startsWith("Sec-CH-", ignoreCase = true) -> " ← Client Hint"
                    key.equals("Cookie", ignoreCase = true) -> " ← Cookies"
                    else -> ""
                }
                log("    $key: $masked$flag")
            }
        }

        // Log all requests to detail file
        detailLog("[${if (isMainFrame) "M" else "S"}] ${request.method} $url")
        request.requestHeaders?.forEach { (key, value) ->
            detailLog("  H> $key: $value")
        }
    }

    /**
     * Log HTTP response details, focusing on Google captcha-related responses.
     */
    fun logResponse(url: String, response: WebResourceResponse?) {
        if (!enabled.get() || response == null) return
        val isGoogleRelated = GOOGLE_PATTERNS.any { url.contains(it, ignoreCase = true) }

        if (isGoogleRelated) {
            log("[RSP] ${response.statusCode} ${response.reasonPhrase} - $url")
            log("  MIME: ${response.mimeType}, Encoding: ${response.encoding}")
            response.responseHeaders?.forEach { (key, value) ->
                val flag = when {
                    key.equals("Set-Cookie", ignoreCase = true) -> " ← Cookie set!"
                    key.equals("Content-Security-Policy", ignoreCase = true) -> " ← CSP"
                    key.equals("X-Frame-Options", ignoreCase = true) -> " ← Frame options"
                    else -> ""
                }
                log("    $key: $value$flag")
            }
        }

        detailLog("[R] ${response.statusCode} $url")
    }

    /**
     * Log when an ad blocker blocks a request (Google captcha resources might be blocked!).
     */
    fun logBlockedRequest(url: String, isMainFrame: Boolean) {
        if (!enabled.get()) return
        val isGoogleRelated = GOOGLE_PATTERNS.any { url.contains(it, ignoreCase = true) }
        if (isGoogleRelated) {
            log("!!! BLOCKED by AdBlocker: $url (MainFrame: $isMainFrame)")
            log("!!! WARNING: This could break Google captcha verification!")
        }
        detailLog("[BLOCKED] $url")
    }

    /**
     * Log when a NetworkEngine handles a request.
     */
    fun logNetworkEngineRequest(url: String, engineName: String) {
        if (!enabled.get()) return
        val isGoogleRelated = GOOGLE_PATTERNS.any { url.contains(it, ignoreCase = true) }
        if (isGoogleRelated) {
            log("[ENGINE] $engineName handled: $url")
        }
        detailLog("[ENGINE:$engineName] $url")
    }

    /**
     * Log JavaScript console messages, focusing on captcha-related output.
     */
    fun logConsoleMessage(level: String, message: String, lineNumber: Int, sourceId: String?) {
        if (!enabled.get()) return
        val isCaptchaRelated = message.contains("recaptcha", ignoreCase = true)
                || message.contains("captcha", ignoreCase = true)
                || message.contains("challenge", ignoreCase = true)
                || message.contains("bot", ignoreCase = true)
                || (sourceId?.contains("recaptcha") == true)
                || (sourceId?.contains("gstatic") == true)

        if (isCaptchaRelated) {
            log("[JS:$level] $message")
            log("  Source: $sourceId:$lineNumber")
        }
        detailLog("[JS:$level] L$lineNumber $message ($sourceId)")
    }

    /**
     * Log page navigation events.
     */
    fun logPageEvent(event: String, url: String, extra: String? = null) {
        if (!enabled.get()) return
        val isGoogleRelated = GOOGLE_PATTERNS.any { url.contains(it, ignoreCase = true) }
        if (isGoogleRelated) {
            log("[PAGE] $event: $url${if (extra != null) " | $extra" else ""}")
        }
        detailLog("[PAGE] $event: $url")
    }

    /**
     * Log cookies for a specific URL.
     */
    fun logCookies(url: String) {
        if (!enabled.get()) return
        val isGoogleRelated = GOOGLE_PATTERNS.any { url.contains(it, ignoreCase = true) }
        if (!isGoogleRelated) return

        try {
            val cookies = CookieManager.getInstance().getCookie(url)
            if (cookies != null) {
                log("[COOKIES] for $url:")
                cookies.split(";").forEach { cookie ->
                    val trimmed = cookie.trim()
                    val name = trimmed.substringBefore("=")
                    val flag = when {
                        name == "NID" -> " ← Google NID (tracking/preference)"
                        name == "SID" || name == "HSID" || name == "SSID" -> " ← Google session"
                        name == "CONSENT" -> " ← Google consent"
                        name == "__Secure-" || name.startsWith("__Secure-") -> " ← Secure cookie"
                        name == "AEC" -> " ← Google anti-abuse"
                        name == "SOCS" -> " ← Google consent state"
                        else -> ""
                    }
                    log("  $trimmed$flag")
                }
            } else {
                log("[COOKIES] No cookies for $url")
            }
        } catch (e: Exception) {
            log("[COOKIES] Error reading cookies: ${e.message}")
        }
    }

    /**
     * Run JavaScript-based fingerprint checks and log results.
     * Call this when a Google page is loaded.
     */
    fun injectFingerprintCheck(webView: WebView) {
        if (!enabled.get()) return
        // This JS will be executed in the page context and results logged via console
        val js = """
        (function() {
            var results = [];
            results.push('=== Captcha Debug: JS Environment ===');
            results.push('navigator.userAgent: ' + navigator.userAgent);
            results.push('navigator.webdriver: ' + navigator.webdriver);
            results.push('navigator.languages: ' + JSON.stringify(navigator.languages));
            results.push('navigator.platform: ' + navigator.platform);
            results.push('navigator.hardwareConcurrency: ' + navigator.hardwareConcurrency);
            results.push('navigator.deviceMemory: ' + (navigator.deviceMemory || 'undefined'));
            results.push('navigator.maxTouchPoints: ' + navigator.maxTouchPoints);
            results.push('navigator.vendor: ' + navigator.vendor);
            results.push('navigator.plugins.length: ' + navigator.plugins.length);
            
            // Check for WebView-specific signals
            results.push('window.Android: ' + (typeof window.Android));
            results.push('window._paq: ' + (typeof window._paq));
            results.push('window.chrome: ' + (typeof window.chrome));
            results.push('window.chrome.runtime: ' + (typeof (window.chrome && window.chrome.runtime)));
            
            // Client Hints API
            if (navigator.userAgentData) {
                results.push('navigator.userAgentData.mobile: ' + navigator.userAgentData.mobile);
                results.push('navigator.userAgentData.platform: ' + navigator.userAgentData.platform);
                results.push('navigator.userAgentData.brands: ' + JSON.stringify(navigator.userAgentData.brands));
                navigator.userAgentData.getHighEntropyValues([
                    'architecture', 'bitness', 'fullVersionList', 'model',
                    'platformVersion', 'uaFullVersion', 'wow64'
                ]).then(function(data) {
                    console.log('CaptchaDebug:HE:' + JSON.stringify(data));
                }).catch(function(e) {
                    console.log('CaptchaDebug:HE:ERROR:' + e.message);
                });
            } else {
                results.push('navigator.userAgentData: NOT AVAILABLE');
            }
            
            // Canvas fingerprint test
            try {
                var canvas = document.createElement('canvas');
                var ctx = canvas.getContext('2d');
                ctx.textBaseline = 'top';
                ctx.font = '14px Arial';
                ctx.fillText('test', 0, 0);
                results.push('Canvas: supported');
            } catch(e) {
                results.push('Canvas: ' + e.message);
            }
            
            // WebGL
            try {
                var canvas2 = document.createElement('canvas');
                var gl = canvas2.getContext('webgl') || canvas2.getContext('experimental-webgl');
                if (gl) {
                    var debugInfo = gl.getExtension('WEBGL_debug_renderer_info');
                    if (debugInfo) {
                        results.push('WebGL Vendor: ' + gl.getParameter(debugInfo.UNMASKED_VENDOR_WEBGL));
                        results.push('WebGL Renderer: ' + gl.getParameter(debugInfo.UNMASKED_RENDERER_WEBGL));
                    }
                    results.push('WebGL: supported');
                } else {
                    results.push('WebGL: not supported');
                }
            } catch(e) {
                results.push('WebGL: ' + e.message);
            }
            
            // Permissions API
            if (navigator.permissions) {
                results.push('Permissions API: available');
            } else {
                results.push('Permissions API: not available');
            }
            
            // Service Worker
            results.push('ServiceWorker: ' + ('serviceWorker' in navigator));
            
            // Notification
            results.push('Notification: ' + (typeof Notification));
            
            // Dump all results
            results.forEach(function(r) {
                console.log('CaptchaDebug:' + r);
            });
        })();
        """.trimIndent()

        webView.evaluateJavascript(js, null)
    }

    /**
     * Convenience method: dump a full snapshot of the current state.
     */
    fun dumpFullSnapshot(webView: WebView, headers: Map<String, String>, url: String) {
        if (!enabled.get()) return
        log("")
        log("#".repeat(80))
        log("# FULL STATE SNAPSHOT at ${dateFormat.format(Date())}")
        log("# URL: $url")
        log("#".repeat(80))
        logWebViewSettings(webView)
        logRequestHeaders(headers)
        logCookies(url)
        log("#".repeat(80))
        log("")
    }

    // ---- Internal helpers ----

    private fun log(message: String) {
        Timber.d("$TAG: $message")
        writeToFile(logFile, message)
    }

    private fun detailLog(message: String) {
        writeToFile(detailLogFile, message)
    }

    @Synchronized
    private fun writeToFile(file: File?, message: String) {
        if (file == null) return
        try {
            PrintWriter(FileWriter(file, true)).use { writer ->
                writer.println("[${dateFormat.format(Date())}] $message")
            }
        } catch (e: Exception) {
            Timber.e(e, "$TAG: Failed to write to ${file.name}")
        }
    }

    private fun rotateIfNeeded(file: File) {
        if (file.exists() && file.length() > MAX_LOG_SIZE) {
            val backup = File(file.parent, "${file.nameWithoutExtension}_prev.${file.extension}")
            backup.delete()
            file.renameTo(backup)
        }
    }

    private fun mixedContentModeString(mode: Int): String = when (mode) {
        WebSettings.MIXED_CONTENT_ALWAYS_ALLOW -> "ALWAYS_ALLOW"
        WebSettings.MIXED_CONTENT_NEVER_ALLOW -> "NEVER_ALLOW"
        WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE -> "COMPATIBILITY_MODE"
        else -> "UNKNOWN($mode)"
    }

    private fun cacheModeString(mode: Int): String = when (mode) {
        WebSettings.LOAD_DEFAULT -> "LOAD_DEFAULT"
        WebSettings.LOAD_CACHE_ELSE_NETWORK -> "CACHE_ELSE_NETWORK"
        WebSettings.LOAD_CACHE_ONLY -> "CACHE_ONLY"
        WebSettings.LOAD_NO_CACHE -> "NO_CACHE"
        else -> "UNKNOWN($mode)"
    }

    fun setEnabled(enable: Boolean) {
        enabled.set(enable)
    }

    fun isEnabled(): Boolean = enabled.get()

    /**
     * Flush and return the log file path for sharing.
     */
    fun getLogFilePath(): String? = logFile?.absolutePath

    fun getDetailLogFilePath(): String? = detailLogFile?.absolutePath
}
