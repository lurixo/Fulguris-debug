# Windows Subsystem for Android (WSA)

## Overview

**Windows Subsystem for Android (WSA)** is Microsoft's solution for running Android applications natively on Windows 11. It provides a convenient testing environment for Fulguris development without requiring physical devices or separate Android emulators.

**Documentation:** https://learn.microsoft.com/en-us/windows/android/wsa/
**License:** Proprietary (Microsoft)

## Why WSA for Fulguris Development

WSA offers several advantages for testing Fulguris on Windows:

- **Native Integration:** Runs Android apps directly on Windows 11
- **No Emulator Overhead:** Better performance than traditional Android emulators
- **Easy Access:** Test Fulguris alongside Windows applications
- **ADB Compatible:** Full ADB support for installation and debugging
- **Resource Efficient:** Suspends automatically when not in use
- **Desktop Experience:** Test Fulguris on desktop-class hardware
- **Windows 11 Features:** Integration with Windows clipboard, file system

## System Requirements

**Minimum:**
- Windows 11 (build 22000.0 or higher)
- 8 GB RAM
- Intel Core i3 8th Gen / AMD Ryzen 3000 / Snapdragon 8c or better
- SSD storage (recommended for performance)
- Virtualization enabled in BIOS/UEFI

**Recommended:**
- 16 GB RAM
- Intel Core i5 or AMD Ryzen 5 (or better)
- Virtual Machine Platform and Windows Hypervisor Platform enabled

**Check Virtualization:**
```powershell
# Open Task Manager → Performance → CPU
# Look for "Virtualization: Enabled"

# Or use PowerShell:
Get-ComputerInfo | Select-Object HyperVRequirementVirtualizationFirmwareEnabled
```

## Installation

### Install WSA from Microsoft Store

1. **Open Microsoft Store**
2. **Search** for "Windows Subsystem for Android™"
   - Full name: "Windows Subsystem for Android™ with Amazon Appstore"
3. **Click Get / Install**
4. **Wait for download** (approximately 1.2 GB)
5. **Launch** Windows Subsystem for Android from Start Menu

### Enable Developer Mode

1. **Open** Windows Subsystem for Android settings
2. **Toggle** Developer mode to **On**
3. **Note the IP address** (usually `127.0.0.1:58526`)
4. Keep WSA settings window open during development

### Enable Required Windows Features

If WSA fails to start, enable these Windows features:

```powershell
# Run PowerShell as Administrator
Enable-WindowsOptionalFeature -Online -FeatureName VirtualMachinePlatform -All
Enable-WindowsOptionalFeature -Online -FeatureName Microsoft-Hyper-V -All

# Restart computer after enabling
```

## Connecting ADB to WSA

### Initial Setup

```powershell
# Ensure WSA is running (check system tray icon)

# Connect to WSA via localhost
adb connect 127.0.0.1:58526

# Expected output:
# connected to 127.0.0.1:58526

# Verify connection
adb devices

# Expected output:
# List of devices attached
# 127.0.0.1:58526    device
```

### Reconnecting After WSA Restart

WSA automatically suspends when no apps are running:

```powershell
# If connection lost, reconnect
adb disconnect 127.0.0.1:58526
adb connect 127.0.0.1:58526
```

### Multiple Devices

If you have other devices connected:

```powershell
# Target WSA specifically
adb -s 127.0.0.1:58526 <command>

# Example:
adb -s 127.0.0.1:58526 install app.apk
```

## Installing and Testing Fulguris on WSA

### Workflow 1: Build and Install

```powershell
# 1. Build debug APK
.\gradlew assembleSlionsFullDownloadDebug

# 2. Connect to WSA
adb connect 127.0.0.1:58526

# 3. Install Fulguris
adb install -r app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk

# 4. Launch Fulguris
adb shell am start -n net.slions.fulguris.full.download/.BrowserActivity
```

### Workflow 2: Quick Reinstall During Development

```powershell
# Build and install in one line
.\gradlew assembleSlionsFullDownloadDebug && adb -s 127.0.0.1:58526 install -r app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk
```

### Workflow 3: Install and Monitor Logs

```powershell
# Terminal 1: Watch logs
adb -s 127.0.0.1:58526 logcat -c
adb -s 127.0.0.1:58526 logcat | Select-String "fulguris"

# Terminal 2: Install and launch
adb -s 127.0.0.1:58526 install -r app.apk
adb -s 127.0.0.1:58526 shell am start -n net.slions.fulguris.full.download/.BrowserActivity
```

### Testing Specific Features

**Test Incognito Mode:**
```powershell
adb -s 127.0.0.1:58526 shell am start -n net.slions.fulguris.full.download/.BrowserActivity --ez incognito true
```

**Clear App Data:**
```powershell
adb -s 127.0.0.1:58526 shell pm clear net.slions.fulguris.full.download
```

**Access App Storage:**
```powershell
# Pull database
adb -s 127.0.0.1:58526 shell "run-as net.slions.fulguris.full.download cat /data/data/net.slions.fulguris.full.download/databases/bookmarks.db" > bookmarks.db

# Pull preferences
adb -s 127.0.0.1:58526 pull /data/data/net.slions.fulguris.full.download/shared_prefs/ ./prefs/
```

## WSA Limitations

### Google Play Services

**Issue:** WSA does not include Google Play Services

**Impact on Fulguris:**
- ❌ Cannot test Play Store variant (`slionsFullPlaystore`)
- ❌ No Firebase Analytics/Crashlytics
- ❌ SafetyNet/Play Integrity checks fail
- ✅ Download and F-Droid variants work fine

**Solution:** Use `slionsFullDownload` or `slionsFullFdroid` variants for WSA testing

### Hardware Sensors

**Limited or Missing:**
- GPS/Location services (may not provide accurate location)
- Accelerometer, gyroscope (orientation detection)
- Camera (may use Windows webcam, but with limitations)
- Fingerprint sensor
- NFC

**Testing Location Features:**
```powershell
# Mock location for testing
adb -s 127.0.0.1:58526 shell settings put secure location_providers_allowed +gps
adb -s 127.0.0.1:58526 shell settings put secure location_providers_allowed +network

# Set mock location (requires mock location app)
```

### Performance Considerations

**Generally Good:**
- UI rendering and responsiveness
- Web page loading
- Basic functionality testing

**May Vary:**
- WebView performance (depends on hardware)
- Video playback
- Complex JavaScript execution
- Heavy DOM manipulation

### Known Issues

- Some Android APIs behave differently than physical devices
- Background service restrictions may be stricter
- Network configuration may differ from mobile networks
- Storage paths may be different

## WSA Settings and Configuration

### WSA Settings App

Access via Start Menu → "Windows Subsystem for Android Settings"

**Key Settings:**

**Subsystem Resources:**
- **Continuous** - WSA always running (better for active development)
- **As needed** - WSA suspends when idle (saves resources)

**Developer:**
- **Developer mode** - Enable for ADB access
- **Manage developer settings** - Opens Android developer options

**Network:**
- Options for network connectivity mode

**Optional features:**
- Amazon Appstore (not needed for sideloading)

### Android Settings Within WSA

Access Android settings:

```powershell
# Open Android settings from WSA
adb -s 127.0.0.1:58526 shell am start -a android.settings.SETTINGS
```

Or launch from Windows Start Menu after WSA is running.

### Files and Storage

**Access Android Files from Windows:**
1. Open WSA Settings
2. Click **Files** under Advanced
3. Opens Android's `/sdcard/` in Windows Explorer

**Or via ADB:**
```powershell
# Push file to Android storage
adb -s 127.0.0.1:58526 push file.txt /sdcard/Download/

# Pull file from Android storage
adb -s 127.0.0.1:58526 pull /sdcard/Download/file.txt ./
```

## Troubleshooting

### WSA Won't Start

**Symptom:** WSA fails to launch or shows error

**Solutions:**
1. **Check virtualization** - Must be enabled in BIOS
2. **Enable Windows features:**
   ```powershell
   # Run as Administrator
   Enable-WindowsOptionalFeature -Online -FeatureName VirtualMachinePlatform -All
   ```
3. **Restart Windows** after enabling features
4. **Check Windows Update** - Ensure Windows 11 is up to date
5. **Reinstall WSA** - Uninstall from Apps & features, then reinstall from Store

### ADB Connection Failed

**Symptom:** `adb connect 127.0.0.1:58526` fails

**Solutions:**
1. **Ensure WSA is running** - Check system tray for WSA icon
2. **Enable Developer mode** - WSA Settings → Developer mode → On
3. **Restart WSA:**
   - WSA Settings → Turn off subsystem
   - Wait 10 seconds
   - Turn on subsystem
4. **Check firewall** - Ensure ADB port is not blocked
5. **Restart ADB server:**
   ```powershell
   adb kill-server
   adb start-server
   adb connect 127.0.0.1:58526
   ```

### App Installation Failed

**Symptom:** `adb install` fails with errors

**Solutions:**

| Error | Solution |
|-------|----------|
| `INSTALL_FAILED_UPDATE_INCOMPATIBLE` | Uninstall existing version first |
| `INSTALL_FAILED_INSUFFICIENT_STORAGE` | Free up WSA storage in settings |
| `INSTALL_FAILED_OLDER_SDK` | Check APK minSdkVersion vs WSA Android version |
| Connection timeout | Ensure WSA is running and reconnect ADB |

### Fulguris Not Launching

**Symptom:** App installs but won't launch

**Solutions:**
1. **Check logcat for errors:**
   ```powershell
   adb -s 127.0.0.1:58526 logcat | Select-String "AndroidRuntime"
   ```
2. **Clear app data:**
   ```powershell
   adb -s 127.0.0.1:58526 shell pm clear net.slions.fulguris.full.download
   ```
3. **Reinstall with correct variant** - Use Download or F-Droid, not Play Store
4. **Check permissions:**
   ```powershell
   adb -s 127.0.0.1:58526 shell pm grant net.slions.fulguris.full.download android.permission.INTERNET
   ```

### Poor Performance

**Symptom:** Fulguris runs slowly on WSA

**Solutions:**
1. **Switch to Continuous mode** - WSA Settings → Subsystem resources → Continuous
2. **Increase RAM allocation** - WSA Settings → Advanced
3. **Close other applications** - Free up system resources
4. **Use SSD storage** - Ensure WSA is on SSD, not HDD
5. **Update GPU drivers** - Better graphics performance

## Tips and Best Practices

### Development Workflow

**Keep WSA in Continuous Mode:**
```
WSA Settings → Subsystem resources → Continuous
```
Prevents WSA from suspending during active development.

**Use Build-Install Script:**

Create `deploy_wsa.ps1`:
```powershell
# Build and deploy to WSA
Write-Host "Building Fulguris..."
.\gradlew assembleSlionsFullDownloadDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host "Connecting to WSA..."
    adb connect 127.0.0.1:58526

    Write-Host "Installing..."
    adb -s 127.0.0.1:58526 install -r app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk

    Write-Host "Launching Fulguris..."
    adb -s 127.0.0.1:58526 shell am start -n net.slions.fulguris.full.download/.BrowserActivity
}
```

**Run:**
```powershell
.\deploy_wsa.ps1
```

### Testing Strategy

**Use WSA For:**
- ✅ UI/UX testing
- ✅ Desktop layout testing
- ✅ Quick iteration during development
- ✅ Accessibility testing with Windows screen readers
- ✅ Basic functionality verification

**Use Physical Device For:**
- ❌ Location-based features
- ❌ Camera/sensor features
- ❌ Performance benchmarking
- ❌ Mobile-specific behaviors
- ❌ Play Store variant testing

### Resource Management

**Stop WSA When Not Needed:**
```
WSA Settings → Turn off subsystem
```
Frees up RAM and CPU resources.

**Monitor WSA Resource Usage:**
- Task Manager → Android (WSA)
- Typically uses 2-4 GB RAM when active

## Comparison: WSA vs Android Emulator vs Physical Device

| Feature | WSA | Android Emulator | Physical Device |
|---------|-----|------------------|-----------------|
| **Setup Time** | Fast (< 5 min) | Medium (10-15 min) | Instant |
| **Performance** | Very Good | Good | Best |
| **Google Play** | ❌ No | ✅ Yes (with Google APIs image) | ✅ Yes |
| **Sensors** | ❌ Limited | ⚠️ Simulated | ✅ Real hardware |
| **ADB Support** | ✅ Yes | ✅ Yes | ✅ Yes |
| **Resource Usage** | Medium | High | None (on PC) |
| **Windows Integration** | ✅ Excellent | ❌ Separate VM | ❌ External |
| **Best For** | Desktop testing, quick iterations | Full Android testing | Real-world testing |

## Related Documentation

- [ADB](/Slion/Fulguris/wiki/ADB) - Android Debug Bridge for device communication
- [Android Studio](/Slion/Fulguris/wiki/Android-Studio) - IDE with emulator management
- [Gradlew](/Slion/Fulguris/wiki/Gradlew) - Building APKs for WSA
- [Building](/Slion/Fulguris/wiki/Building) - Build variants and configurations

## External Links

- **Official Documentation:** https://learn.microsoft.com/en-us/windows/android/wsa/
- **Microsoft Store:** https://www.microsoft.com/store/productId/9P3395VX91NR
- **Community Forum:** https://github.com/microsoft/WSA/discussions
- **Known Issues:** https://learn.microsoft.com/en-us/windows/android/wsa/#known-issues

---

**Last Updated:** December 21, 2025
**Maintained by:** Fulguris Development Team
