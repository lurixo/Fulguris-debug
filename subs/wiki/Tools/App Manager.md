# App Manager

## Overview

**App Manager** is a comprehensive, open-source Android package manager and viewer that provides advanced tools for managing, debugging, and inspecting Android applications. It's particularly useful for Fulguris development and testing.

**GitHub:** https://github.com/MuntashirAkon/AppManager  
**License:** GPL-3.0

## Why App Manager for Fulguris Development

App Manager provides deep insights into Fulguris's internal state, permissions, storage, and behavior that are essential for development and debugging:

- **Permission Management:** View and revoke permissions (location, camera, storage, etc.)
- **Storage Inspection:** Examine app data, cache, and databases
- **Component Analysis:** View activities, services, receivers, and providers
- **Backup/Restore:** Test data migration and backup scenarios
- **App Operations:** Force stop, clear data, uninstall test builds
- **APK Analysis:** Inspect manifest, resources, and signing information
- **Network Monitoring:** Track network usage and connections
- **Battery Optimization:** Test doze mode and background restrictions

## Installation

### From F-Droid (Recommended)
1. Open F-Droid app store
2. Search for "App Manager"
3. Install the latest version

### From GitHub Releases
1. Visit https://github.com/MuntashirAkon/AppManager/releases
2. Download the latest APK file
3. Install via ADB: `adb install AppManager-v*.apk`

### Granting Root/ADB Permissions (Optional)
For full functionality, App Manager can use root or ADB permissions:

```bash
# Grant ADB permissions (no root required)
adb shell sh /sdcard/Android/data/io.github.muntashirakon.AppManager/files/enable-am.sh
```

## Key Features for Fulguris Testing

### 1. Permission Inspection

**Use Case:** Verify location, storage, camera permissions are correctly requested and granted.

**Steps:**
1. Open App Manager
2. Search for "Fulguris"
3. Go to **App Info** tab
4. Select **Permissions** section
5. Review all declared and granted permissions

**What to Check:**
- Location permissions (fine/coarse)
- Storage permissions (read/write)
- Camera/microphone permissions
- Internet permission
- Foreground service permission

### 2. Storage and Data Management

**Use Case:** Inspect SharedPreferences, databases, cache, and user data.

**Steps:**
1. Open App Manager → Fulguris
2. Go to **App Info** tab
3. Select **Data** section
4. View data directories:
   - **App Data:** `/data/data/net.slions.fulguris.*/`
   - **External Storage:** `/sdcard/Android/data/net.slions.fulguris.*/`
   - **Cache:** Cache directory size and contents

**Common Files to Inspect:**
- `shared_prefs/*.xml` - Settings and preferences
- `databases/*.db` - Bookmarks, history, sessions
- `cache/` - Cached web content
- `files/` - Downloaded files, custom data

### 3. Component Analysis

**Use Case:** View all activities, services, receivers, and content providers.

**Steps:**
1. Open App Manager → Fulguris
2. Go to **Activities** tab
3. Browse components:
   - **Activities:** All UI screens and dialogs
   - **Services:** Background services (download, incognito notification)
   - **Receivers:** Broadcast receivers
   - **Providers:** Content providers

**What to Check:**
- Exported components (security check)
- Intent filters
- Launch modes
- Process names

### 4. Backup and Restore

**Use Case:** Test data migration, backup scenarios, and restore functionality.

**Steps:**
1. Open App Manager → Fulguris
2. Tap **Backup/Restore** option
3. Create backup:
   - APK + Data
   - Data only
   - APK only
4. Install fresh build and restore backup

**Testing Scenarios:**
- Upgrade from older version
- Restore settings after reinstall
- Migrate data between variants (full/fdroid/playstore)

### 5. App Operations

**Use Case:** Quickly reset app state, force stop, or clear cache during testing.

**Steps:**
1. Open App Manager → Fulguris
2. Use quick actions:
   - **Force Stop:** Stop all processes immediately
   - **Clear Data:** Reset app to fresh state
   - **Clear Cache:** Remove cached web content
   - **Uninstall:** Remove app completely

**When to Use:**
- Reset app state between test runs
- Clear cache to test fresh page loads
- Force stop to test app restart behavior

### 6. APK and Manifest Analysis

**Use Case:** Inspect compiled resources, manifest entries, and signing information.

**Steps:**
1. Open App Manager → Fulguris
2. Go to **App Info** tab
3. Select **APK** section
4. View:
   - Manifest XML
   - Resources (strings, layouts)
   - Native libraries
   - Signing certificate
   - DEX classes

**What to Check:**
- Correct version code and name
- Proper signing certificate (debug vs release)
- Required permissions declared
- Min/target SDK versions
- ProGuard/R8 obfuscation

### 7. Network Monitoring

**Use Case:** Track network usage and active connections.

**Steps:**
1. Open App Manager → Fulguris
2. Go to **App Usage** tab
3. View network statistics:
   - Mobile data usage
   - WiFi data usage
   - Active connections
   - Network permissions

**Testing Scenarios:**
- Verify data saver mode reduces usage
- Check background data restrictions
- Monitor download manager network activity

### 8. Battery and Background Restrictions

**Use Case:** Test doze mode, background restrictions, and battery optimization.

**Steps:**
1. Open App Manager → Fulguris
2. Go to **App Info** tab
3. Check battery optimization status
4. View background restrictions
5. Test with different optimization settings

**Testing Scenarios:**
- Download behavior in doze mode
- Background tab refresh restrictions
- Notification delivery when optimized

## Common Test Scenarios

### Scenario 1: Testing Location Permissions
1. Open App Manager → Fulguris → Permissions
2. Note current location permission status
3. Open Fulguris and visit https://browserleaks.com/geo
4. Grant location permission when prompted
5. Return to App Manager
6. Verify "Location" shows as granted with timestamp

### Scenario 2: Debugging Storage Issues
1. Open App Manager → Fulguris → Data
2. Check storage usage breakdown
3. Navigate to `/data/data/net.slions.fulguris.*/databases/`
4. Export database files for inspection
5. Use SQLite viewer to examine bookmarks/history
6. Clear data and verify clean state

### Scenario 3: Testing Build Variants
1. Install multiple Fulguris variants:
   - `net.slions.fulguris.full.playstore`
   - `net.slions.fulguris.full.download`
   - `net.slions.fulguris.full.fdroid`
2. Use App Manager to compare:
   - Package names
   - Signing certificates
   - Version codes
   - Installed locations

### Scenario 4: Backup Before Testing
1. Open App Manager → Fulguris
2. Create full backup (APK + Data)
3. Perform destructive testing (clear data, modify settings)
4. Restore from backup
5. Verify all settings and data restored correctly

## Integration with Development Workflow

### Pre-Testing Checklist
1. **Check Permissions:** Verify only required permissions declared
2. **Inspect Manifest:** Confirm correct version and metadata
3. **Review Components:** Ensure no unintended exported components
4. **Check Signing:** Verify correct signing certificate

### During Testing
1. **Monitor Storage:** Track database growth and cache usage
2. **Check Permissions:** Verify runtime permissions requested correctly
3. **Force Stop/Restart:** Test app state management
4. **Clear Cache:** Test fresh page loads

### Post-Testing Cleanup
1. **Clear Data:** Reset app to clean state
2. **Uninstall Test Builds:** Remove debug/beta versions
3. **Restore Backup:** Return to stable configuration

## Debugging Common Issues

### Permission Denied Errors
1. Open App Manager → Fulguris → Permissions
2. Check if permission is declared in manifest (App Info → Manifest)
3. Verify permission is granted at runtime
4. Check for permission revocation in system settings

### Storage Full / Database Errors
1. Open App Manager → Fulguris → Data
2. Check storage usage breakdown
3. Navigate to database directory
4. Export databases for inspection with SQLite tools
5. Clear data if corrupted

### Crash on Startup
1. Open App Manager → Fulguris → App Info
2. Check minimum SDK version matches device
3. View native libraries (check architecture compatibility)
4. Check logcat output via App Manager
5. Clear data and test fresh install

### Background Service Not Working
1. Open App Manager → Fulguris → Services
2. Check if services are registered in manifest
3. Verify battery optimization settings
4. Check background restriction status
5. Test with battery optimization disabled

## Advanced Features

### Batch Operations
- Backup multiple app versions
- Compare manifests between builds
- Export all data for analysis

### Root-Only Features (Optional)
- Modify app data directly
- Disable/enable components
- Grant/revoke any permission
- View private system data

### ADB Mode Features
- Full access without root
- Remote debugging capabilities
- Scripting and automation support

## Important Notes

- **Privacy:** App Manager has full access to app data - use on development devices only
- **Root:** Not required for most features, but provides additional capabilities
- **Backup Location:** Default backup directory is `/sdcard/AppManager/`
- **Performance:** Large apps may take time to analyze completely
- **Permissions:** App Manager itself requires storage permission to create backups

## Related Documentation

- [BrowserLeaks](/Slion/Fulguris/wiki/browserleaks.com) - Web-based testing tool
- [Geolocation](/Slion/Fulguris/wiki/Geolocation) - Location permission implementation
- [Privacy](/Slion/Fulguris/wiki/Privacy) - Privacy feature development

## External Links

- **GitHub Repository:** https://github.com/MuntashirAkon/AppManager
- **Documentation:** https://muntashirakon.github.io/AppManager/
- **F-Droid:** https://f-droid.org/packages/io.github.muntashirakon.AppManager/
- **User Guide:** https://muntashirakon.github.io/AppManager/guide/

---

**Last Updated:** December 21, 2025  
**Maintained by:** Fulguris Development Team
