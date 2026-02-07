Fulguris is using [WebView] to display web pages. [WebView] is part of [Chromium] which is the engine [Chrome] is based on. Therefore Fulguris is arguably just as fast and secure as the most popular web browser on earth.

# Webkit

[WebView] APIs are published through Jetpack [Webkit]. Checkout the [Webkit] release notes to keep on top of new features.

# DevTools
Fulguris provides easy access to WebView [DevTools] from *Menu > Settings > About > WebView version*.

## WebView Flags
From the WebView [DevTools] you can change so called Flags allowing you to tweak your [WebView] behaviour.
Here we list some notable Flags you may want to try out. After changing your Flags make sure you restart Fulguris for them to take effect.

### SwipeToMoveCursor
From Android 11 you can enable this flag. It will make it easier to reposition your cursor in text fields.
Just swipe within a focused text field filled with text and see how you can now move your cursor easily.

## Change WebView Provider
From the menu of your WebView [DevTools] you can *Change WebView Provider*.
That will let you select your WebView implementation.
Though actually installing WebView implementations other than the Android System WebView can be tricky.
This feature is also accessible from your [developer options].

# WebView implementations
Your Android device should have an Android System WebView pre-installed. By default that could be the only implementation you have access to.
However there are a number of other WebView implementations available on Google Play.
Below we list some well-known [WebView implementations], from the more stable ones to the experimental ones:

| Name  | Package | Provider |
| ------------- | ------------- | ------------- |
| Android System WebView | com.android.webview  | Firmware |
| Android System WebView | com.google.android.webview | Google Play |
| Android System WebView Beta | com.google.android.webview.beta | Google Play |
| Android System WebView Dev | com.google.android.webview.dev | Google Play |
| Android System WebView Canary | com.google.android.webview.canary | Google Play |

Once installed such WebView implementation can be selected from [DevTools] as WebView provider. However other third-party WebView implementation such as the one from Brave cannot be used without building your own Android ROM image. 

## Install alternate WebView
Android devices [restrict the packages] that [can provide WebView] implementations. However if you build your own Android ROM image you can extend it by adding [Brave] package name for instance – *com.brave.browser*. All you need to do is [configure the Android framework] accordingly.

# References
- [Managing WebView]
- [Building web apps in WebView] 
- [WebView docs]

[WebView]: https://www.chromium.org/developers/androidwebview
[Webkit]: https://developer.android.com/jetpack/androidx/releases/webkit
[Chromium]: https://www.chromium.org
[Chrome]: https://www.google.com/chrome/
[DevTools]: https://chromium.googlesource.com/chromium/src/+/HEAD/android_webview/docs/developer-ui.md
[developer options]: https://developer.android.com/studio/debug/dev-options
[restrict the packages]: https://chromium.googlesource.com/chromium/src/+/HEAD/android_webview/docs/build-instructions.md#Changing-package-name
[can provide WebView]: https://chromium.googlesource.com/chromium/src/+/HEAD/android_webview/docs/webview-providers.md#Package-name
[configure the Android framework]: https://chromium.googlesource.com/chromium/src/+/HEAD/android_webview/docs/aosp-system-integration.md#Configuring-the-Android-framework
[Brave]: https://brave.com
[Managing WebView]: https://developer.android.com/guide/webapps/managing-webview
[Building web apps in WebView]: https://developer.android.com/guide/webapps/webview
[WebView implementations]: https://chromium.googlesource.com/chromium/src/+/HEAD/android_webview/docs/prerelease.md
[WebView docs]: https://chromium.googlesource.com/chromium/src/+/HEAD/android_webview/docs/README.md