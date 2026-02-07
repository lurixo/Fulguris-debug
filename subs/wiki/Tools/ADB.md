# Android Debug Bridge (ADB)

## Overview

**Android Debug Bridge (ADB)** is a command-line tool that enables communication between your development machine and Android devices or emulators. It's an essential tool for Fulguris development, providing capabilities for installing apps, debugging, accessing device logs, and managing files.

**Documentation:** https://developer.android.com/tools/adb
**License:** Apache License 2.0 (part of Android SDK Platform-Tools)

## Why ADB for Fulguris Development

ADB is indispensable for Android development and testing:

- **App Installation:** Install/uninstall APKs directly from command line
- **Debugging:** Access logcat for real-time debugging output
- **File Management:** Push/pull files to/from device storage
- **Shell Access:** Execute commands directly on device
- **Screen Capture:** Take screenshots and record screen
- **Performance Analysis:** Profile CPU, memory, and battery usage
- **Network Tools:** Port forwarding and reverse proxying
- **Device Management:** Reboot, unlock bootloader, sideload updates

## Installation

### Via Android Studio (Recommended)

ADB is included with Android SDK Platform-Tools, automatically installed with Android Studio:

**Location:**
- **Windows:** `C:\Users\{username}\AppData\Local\Android\Sdk\platform-tools\`
- **macOS:** `~/Library/Android/sdk/platform-tools/`
- **Linux:** `~/Android/Sdk/platform-tools/`

**Verify Installation:**
```powershell
# Check if adb is in PATH
adb version

# Output should show:
# Android Debug Bridge version 1.0.41
# Version 35.0.0-...
```

### Adding ADB to PATH (Windows)

If `adb` command is not found, add platform-tools to system PATH:

**PowerShell (Current Session):**
```powershell
$env:PATH += ";C:\Users\$env:USERNAME\AppData\Local\Android\Sdk\platform-tools"
```

**Permanent (System Settings):**
1. Open **System Properties** → **Advanced** → **Environment Variables**
2. Under **User variables**, select **Path** → **Edit**
3. Click **New** and add: `C:\Users\{YourUsername}\AppData\Local\Android\Sdk\platform-tools`
4. Click **OK** to save
5. Restart PowerShell/Command Prompt

### Standalone Installation (Without Android Studio)

**Download Platform-Tools:**
1. Visit: https://developer.android.com/tools/releases/platform-tools
2. Download ZIP for your platform
3. Extract to a directory (e.g., `C:\platform-tools`)
4. Add to PATH as described above

## Device Setup

### Enable USB Debugging

**On Android Device:**
1. Open **Settings** → **About phone**
2. Tap **Build number** 7 times (enables Developer Options)
3. Go back to **Settings** → **System** → **Developer options**
4. Enable **USB debugging**
5. (Optional) Enable **Stay awake** - Keeps screen on while charging

### Connect Device

**Via USB Cable:**
1. Connect device to computer via USB
2. On device, allow USB debugging when prompted
3. Check "Always allow from this computer" (optional)
4. Tap **Allow**

**Verify Connection:**
```powershell
adb devices

# Output:
# List of devices attached
# 1A2B3C4D5E6F    device
```

**Device States:**
- `device` - Fully connected and authorized
- `unauthorized` - Waiting for authorization on device
- `offline` - Device is not responding
- `no devices/emulators found` - No device connected

### Wireless Debugging (Android 11+)

**Enable Wireless Debugging:**
1. Ensure device and computer are on same WiFi network
2. Settings → Developer options → Wireless debugging → Enable
3. Tap **Pair device with pairing code**
4. Note IP address, port, and pairing code

**Pair Device:**
```powershell
# Pair using code (one-time setup)
adb pair <IP>:<PORT>
# Enter pairing code when prompted

# Example:
adb pair 192.168.1.100:37891
```

**Connect Wirelessly:**
```powershell
# Connect to device
adb connect <IP>:<PORT>

# Example:
adb connect 192.168.1.100:5555

# Verify connection
adb devices
```

**Disconnect:**
```powershell
adb disconnect <IP>:<PORT>
```

## Essential ADB Commands

### Device Management

```powershell
# List connected devices
adb devices

# List devices with detailed info
adb devices -l

# Connect to specific device (if multiple connected)
adb -s <device_id> <command>

# Example:
adb -s 1A2B3C4D5E6F shell

# Reboot device
adb reboot

# Reboot to bootloader
adb reboot bootloader

# Reboot to recovery
adb reboot recovery

# Kill ADB server (useful when adb hangs)
adb kill-server

# Start ADB server
adb start-server
```

### App Installation and Management

```powershell
# Install APK
adb install path/to/app.apk

# Install with specific options
adb install -r app.apk         # Reinstall (keep data)
adb install -d app.apk         # Allow version downgrade
adb install -g app.apk         # Grant all permissions
adb install -t app.apk         # Allow test APKs

# Install Fulguris debug build
adb install app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk

# Uninstall app
adb uninstall <package_name>

# Example:
adb uninstall net.slions.fulguris.full.download

# Uninstall but keep data
adb uninstall -k net.slions.fulguris.full.download

# List installed packages
adb shell pm list packages

# List Fulguris packages
adb shell pm list packages | Select-String fulguris

# Get path to installed APK
adb shell pm path net.slions.fulguris.full.download

# Clear app data (reset app)
adb shell pm clear net.slions.fulguris.full.download

# Force stop app
adb shell am force-stop net.slions.fulguris.full.download

# Launch app (main activity)
adb shell am start -n net.slions.fulguris.full.download/.BrowserActivity
```

### Logging and Debugging

```powershell
# View logcat (all logs)
adb logcat

# Filter by package name
adb logcat | Select-String "fulguris"

# Filter by log level (Error, Warning, Info, Debug, Verbose)
adb logcat *:E          # Errors only
adb logcat *:W          # Warnings and above
adb logcat *:I          # Info and above

# Filter by tag
adb logcat -s "WebView"

# Clear logcat buffer
adb logcat -c

# Save logcat to file
adb logcat > logcat_output.txt

# View specific buffer (main, system, radio, events, crash)
adb logcat -b crash     # Crash logs

# Continuous log with timestamp
adb logcat -v time

# Fulguris-specific debugging
adb logcat | Select-String "net.slions.fulguris"
```

### File Transfer

```powershell
# Push file to device
adb push local_file.txt /sdcard/Download/

# Pull file from device
adb pull /sdcard/Download/file.txt ./

# Push Fulguris database for testing
adb push test_database.db /sdcard/Android/data/net.slions.fulguris.full.download/files/databases/

# Pull Fulguris preferences
adb pull /data/data/net.slions.fulguris.full.download/shared_prefs/ ./prefs/

# Note: Pulling from /data/data/ requires root or debuggable app
```

### Device Shell

```powershell
# Open interactive shell
adb shell

# Execute single command
adb shell <command>

# Examples:
adb shell ls /sdcard/Download/
adb shell cat /proc/cpuinfo
adb shell getprop ro.build.version.release    # Android version
adb shell dumpsys battery                      # Battery info
adb shell screencap -p /sdcard/screenshot.png  # Screenshot
adb shell screenrecord /sdcard/demo.mp4        # Record screen
```

### Screen Capture

```powershell
# Take screenshot
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png ./

# One-line screenshot (PowerShell)
adb exec-out screencap -p > screenshot.png

# Record screen video (Ctrl+C to stop)
adb shell screenrecord /sdcard/demo.mp4
adb pull /sdcard/demo.mp4 ./

# Record with options
adb shell screenrecord --time-limit 30 --bit-rate 6000000 /sdcard/demo.mp4
```

### System Information

```powershell
# Android version
adb shell getprop ro.build.version.release

# Device model
adb shell getprop ro.product.model

# Device manufacturer
adb shell getprop ro.product.manufacturer

# Screen density
adb shell wm density

# Screen size
adb shell wm size

# Battery status
adb shell dumpsys battery

# Memory info
adb shell dumpsys meminfo

# CPU info
adb shell cat /proc/cpuinfo

# Storage info
adb shell df

# List all system properties
adb shell getprop
```

## Common Fulguris Development Tasks

### Task 1: Install and Launch Fulguris Debug Build

```powershell
# Build APK (from project root)
./gradlew assembleSlionsFullDownloadDebug

# Install APK
adb install -r app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk

# Launch Fulguris
adb shell am start -n net.slions.fulguris.full.download/.BrowserActivity

# View logs immediately
adb logcat -c && adb logcat | Select-String "fulguris"
```

### Task 2: Debug Fulguris Crashes

```powershell
# Clear old logs
adb logcat -c

# Reproduce crash in app

# View crash logs
adb logcat -b crash

# Or filter for exceptions
adb logcat | Select-String "AndroidRuntime"

# Save crash log to file
adb logcat -d > crash_log.txt
```

### Task 3: Test Fulguris with Fresh State

```powershell
# Clear app data (deletes all settings, bookmarks, history)
adb shell pm clear net.slions.fulguris.full.download

# Launch app
adb shell am start -n net.slions.fulguris.full.download/.BrowserActivity
```

### Task 4: Extract Fulguris Database for Analysis

```powershell
# Pull bookmarks database
adb pull /data/data/net.slions.fulguris.full.download/databases/bookmarks.db ./

# Note: Requires debuggable app or root access

# For debuggable builds, use run-as:
adb shell "run-as net.slions.fulguris.full.download cat /data/data/net.slions.fulguris.full.download/databases/bookmarks.db" > bookmarks.db
```

### Task 5: Test Fulguris with Custom User Agent

```powershell
# Launch app with custom user agent via intent
adb shell am start -n net.slions.fulguris.full.download/.BrowserActivity -d "https://browserleaks.com/user-agent"

# Or modify settings via adb shell (requires root or debuggable)
adb shell "run-as net.slions.fulguris.full.download sqlite3 /data/data/net.slions.fulguris.full.download/databases/preferences.db \"UPDATE preferences SET value='CustomUA' WHERE key='user_agent'\""
```

### Task 6: Monitor Fulguris Network Activity

```powershell
# Monitor all logcat output related to network
adb logcat | Select-String "HTTP|URL|Network|WebView"

# Monitor specific WebView logs
adb logcat -s "WebView"

# Capture traffic with network profiler
# Use Android Studio → View → Tool Windows → Profiler → Network
```

### Task 7: Test Fulguris Permissions

```powershell
# List permissions for Fulguris
adb shell dumpsys package net.slions.fulguris.full.download | Select-String "permission"

# Grant permission
adb shell pm grant net.slions.fulguris.full.download android.permission.ACCESS_FINE_LOCATION

# Revoke permission
adb shell pm revoke net.slions.fulguris.full.download android.permission.ACCESS_FINE_LOCATION

# Grant all permissions (test builds)
adb install -g app.apk
```

### Task 8: Benchmark Fulguris Performance

```powershell
# Monitor memory usage
adb shell dumpsys meminfo net.slions.fulguris.full.download

# Monitor CPU usage
adb shell top -m 10 | Select-String "fulguris"

# Monitor battery usage
adb shell dumpsys batterystats net.slions.fulguris.full.download

# Reset battery stats (requires root)
adb shell dumpsys batterystats --reset
```

## Advanced ADB Techniques

### Port Forwarding

Forward traffic from device port to local machine:

```powershell
# Forward device port 8080 to localhost:8080
adb forward tcp:8080 tcp:8080

# List active forwardings
adb forward --list

# Remove all forwardings
adb forward --remove-all
```

**Use Case:** Debug WebView content via Chrome DevTools

### Reverse Port Forwarding

Forward traffic from local machine to device:

```powershell
# Reverse forward localhost:8080 to device port 8080
adb reverse tcp:8080 tcp:8080

# Remove all reverse forwardings
adb reverse --remove-all
```

**Use Case:** Test Fulguris against local development server

### Scripting with ADB

**PowerShell Script Example:**

```powershell
# build_and_deploy.ps1
# Build, install, and launch Fulguris

Write-Host "Building Fulguris..."
./gradlew assembleSlionsFullDownloadDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host "Installing on device..."
    adb install -r app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk

    if ($LASTEXITCODE -eq 0) {
        Write-Host "Launching Fulguris..."
        adb shell am start -n net.slions.fulguris.full.download/.BrowserActivity

        Write-Host "Watching logs..."
        adb logcat -c
        adb logcat | Select-String "fulguris"
    }
}
```

**Run Script:**
```powershell
.\build_and_deploy.ps1
```

### Batch Device Operations

**Install on Multiple Devices:**

```powershell
# Get list of devices
$devices = adb devices | Select-String -Pattern "^\w+" | ForEach-Object { $_.Matches.Value }

# Install on each device
foreach ($device in $devices) {
    Write-Host "Installing on $device"
    adb -s $device install -r app.apk
}
```

## Troubleshooting

### Device Not Detected

**Symptom:** `adb devices` shows no devices

**Solutions:**
1. **Check USB cable** - Use data cable, not charge-only
2. **Enable USB debugging** - Settings → Developer options → USB debugging
3. **Restart ADB server:**
   ```powershell
   adb kill-server
   adb start-server
   ```
4. **Install USB drivers** (Windows only) - Download from device manufacturer
5. **Try different USB port** - Use USB 2.0 ports if USB 3.0 causes issues
6. **Check USB connection mode** - Change to "File Transfer" or "MTP" mode

### Device Shows as Unauthorized

**Symptom:** `adb devices` shows `unauthorized`

**Solutions:**
1. **Check device screen** - Authorization prompt may be waiting
2. **Revoke USB debugging authorizations:**
   - Settings → Developer options → Revoke USB debugging authorizations
   - Reconnect device and re-authorize
3. **Restart ADB server:**
   ```powershell
   adb kill-server
   adb start-server
   ```

### Permission Denied Errors

**Symptom:** `Permission denied` when accessing `/data/data/`

**Solutions:**
1. **Use `run-as`** for debuggable apps:
   ```powershell
   adb shell "run-as net.slions.fulguris.full.download ls /data/data/net.slions.fulguris.full.download/"
   ```
2. **Ensure debug build** - Release builds are not debuggable
3. **Grant storage permissions:**
   ```powershell
   adb shell pm grant net.slions.fulguris.full.download android.permission.WRITE_EXTERNAL_STORAGE
   ```

### ADB Server Out of Date

**Symptom:** `adb server version doesn't match this client`

**Solution:**
```powershell
# Kill all ADB processes
adb kill-server

# Restart server
adb start-server
```

### Logcat No Output

**Symptom:** `adb logcat` shows no output

**Solutions:**
1. **Clear buffer and try again:**
   ```powershell
   adb logcat -c
   adb logcat
   ```
2. **Check device connection:**
   ```powershell
   adb devices
   ```
3. **Check log level filter:**
   ```powershell
   adb logcat *:V    # Verbose (all logs)
   ```

### Installation Failed

**Symptom:** `INSTALL_FAILED_*` errors

**Common Solutions:**

| Error | Cause | Solution |
|-------|-------|----------|
| `INSTALL_FAILED_ALREADY_EXISTS` | App already installed | Use `-r` flag: `adb install -r app.apk` |
| `INSTALL_FAILED_INSUFFICIENT_STORAGE` | Not enough space | Free up device storage |
| `INSTALL_FAILED_VERSION_DOWNGRADE` | Newer version installed | Use `-d` flag: `adb install -d app.apk` |
| `INSTALL_FAILED_UPDATE_INCOMPATIBLE` | Signature mismatch | Uninstall old app first |
| `INSTALL_FAILED_TEST_ONLY` | Test APK | Use `-t` flag: `adb install -t app.apk` |

## Tips and Best Practices

### Workflow Optimization

**Create Aliases:**

Add to PowerShell profile (`$PROFILE`):
```powershell
# Quick adb shortcuts
function adb-install { adb install -r $args }
function adb-log { adb logcat -c; adb logcat | Select-String $args }
function adb-fulguris { adb shell am start -n net.slions.fulguris.full.download/.BrowserActivity }
```

**Use Tab Completion:**
- PowerShell supports tab completion for file paths
- Use `Tab` to auto-complete device IDs, package names, paths

### Debugging Tips

**Filter Logcat Effectively:**
```powershell
# Multiple filters
adb logcat | Select-String "fulguris|ERROR|FATAL"

# Exclude noise
adb logcat | Select-String "fulguris" | Select-String -NotMatch "verbose_tag"

# Color-coded errors (requires external tool like grep)
# Or use Android Studio logcat with color highlighting
```

**Save Logs for Bug Reports:**
```powershell
# Capture logs with timestamp
adb logcat -v time > "fulguris_bug_$(Get-Date -Format 'yyyyMMdd_HHmmss').txt"
```

### Performance

**Wireless ADB:**
- Slightly slower than USB
- Use USB for large file transfers (APKs, databases)
- Wireless is convenient for desk-free testing

**Keep ADB Updated:**
- New Android versions may require updated platform-tools
- Check for updates: Android Studio → SDK Manager → SDK Tools → Android SDK Platform-Tools

## Related Documentation

- [Android Studio](/Slion/Fulguris/wiki/Android-Studio) - IDE with built-in ADB integration
- [Gradlew](/Slion/Fulguris/wiki/Gradlew) - Build tool for compiling APKs
- [App Manager](/Slion/Fulguris/wiki/App-Manager) - Device-side package management
- [Building](/Slion/Fulguris/wiki/Building) - Build and deployment process

## External Links

- **Official Documentation:** https://developer.android.com/tools/adb
- **Command Reference:** https://developer.android.com/tools/adb#shellcommands
- **Platform Tools:** https://developer.android.com/tools/releases/platform-tools
- **Wireless Debugging:** https://developer.android.com/tools/adb#wireless

---

**Last Updated:** December 21, 2025
**Maintained by:** Fulguris Development Team
