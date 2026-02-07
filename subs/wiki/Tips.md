Tips and tricks useful for Android Web Browser developers.

Our example commands typically use Chrome package name: `com.android.chrome`.

# App management

## Open app info settings

`adb shell am start -a android.settings.APPLICATION_DETAILS_SETTINGS -d package:com.android.chrome`

## Disable an app

⚠️ Disabling an app clears all its data storage

`adb shell pm disable-user --user 0 com.android.chrome`.

## Enable an app

`adb shell pm enable com.android.chrome`

## Open default apps settings

`adb shell am start -a android.settings.MANAGE_DEFAULT_APPS_SETTINGS`

# Default browser

## Check default browser

`adb shell cmd package resolve-activity --brief -a android.intent.action.VIEW -d http://www.example.com`

##  Launch default browser

If no default available it will open the chooser:

`adb shell am start -a android.intent.action.VIEW -d http://www.example.com`

## Reset default browser

Some devices won't let you simply clear your default Browser application through settings. You should be able to clear it by running `adb shell pm clear <package-name>` where `package-name` is your current default browser. However that did not even work on HONOR Magic V2 for instance.

Here is my workaround using device UI:
- Set an app as default browser
- Open that app info settings
- Disable that app - ⚠️ Disabling an app clears all app data

The downside is that you can't use that browser unless you re-enable it. I'm usually doing that using Chrome `com.android.chrome` since I typically have Chrome beta installed and a bunch of other browsers I can use instead.

## Launch search query using default search app

`adb shell am start -a android.intent.action.WEB_SEARCH --es query "test"`

## Launch search query using Fulguris debug

`adb shell am start -n net.slions.fulguris.full.download.debug/fulguris.activity.MainActivity -a android.intent.action.WEB_SEARCH --es query "test search"`

## Reset default search app on HONOR

- Open Settings → Apps → Default apps
- Find whichever app is currently the default search handler (likely Google or Chrome)
- Tap it and select Clear defaults
- Next time you trigger a search intent, you'll see the chooser dialog