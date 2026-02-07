# Android Studio

## Overview

**Android Studio** is the official Integrated Development Environment (IDE) for Android app development, built on JetBrains' IntelliJ IDEA platform. It's the primary development tool for building, testing, and debugging Fulguris.

**Website:** https://developer.android.com/studio  
**License:** Apache License 2.0 (IDE), various licenses for SDK components

## Why Android Studio for Fulguris

Android Studio provides comprehensive tools specifically designed for Android development:

- **Kotlin Support:** First-class support for Kotlin, the primary language used in Fulguris
- **Gradle Integration:** Seamless build system integration with dependency management
- **Layout Editor:** Visual tools for designing and previewing XML layouts
- **Debugger:** Powerful debugging tools with breakpoints, variable inspection, and step execution
- **Profiler:** Performance analysis for CPU, memory, network, and battery usage
- **Logcat:** Real-time log viewer for debugging and monitoring
- **Device Manager:** Emulator and physical device management
- **Code Completion:** Intelligent code suggestions and refactoring tools
- **Version Control:** Integrated Git support for managing code changes

## Installation

### System Requirements

**Minimum:**
- 8 GB RAM (16 GB recommended)
- 8 GB of available disk space (IDE + Android SDK + Emulator)
- 1280 x 800 minimum screen resolution

**Supported Platforms:**
- Windows 10/11 (64-bit)
- macOS 10.14 (Mojave) or higher
- Linux (64-bit; tested on Ubuntu, Debian)

### Download and Install

1. **Visit:** https://developer.android.com/studio
2. **Download** the installer for your platform
3. **Run the installer** and follow the setup wizard
4. **Install Android SDK** components (automatically prompted on first launch)
5. **Configure JDK** (bundled JDK is included, or specify custom JDK path)

### Initial Configuration

After installation, configure these essential components:

```
SDK Platforms:
- Android 14 (API 34) - Current target SDK
- Android 13 (API 33)
- Android 12 (API 31)
- Android 11 (API 30)
- Android 8.0 (API 26) - Minimum SDK for Fulguris

SDK Tools:
- Android SDK Build-Tools (latest)
- Android Emulator
- Android SDK Platform-Tools
- Google Play services
- Intel x86 Emulator Accelerator (HAXM) - for Windows/Mac
```

## Opening Fulguris Project

### First Time Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Slion/Fulguris.git
   cd Fulguris
   ```

2. **Open in Android Studio:**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the Fulguris directory
   - Click "OK"

3. **Wait for Gradle sync:**
   - Android Studio will automatically sync Gradle dependencies
   - This may take several minutes on first run
   - Check "Build" output window for progress

4. **Configure local.properties:**
   - Create `local.properties` in the project root (if not exists)
   - Add SDK location (usually auto-detected):
     ```properties
     sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
     ```

### Build Variants

Fulguris has multiple build variants for different distribution channels:

**Product Flavors:**
- `slions` - Base flavor with Slions branding
- `styx` - Alternative branding (legacy)

**Distribution Variants:**
- `full` - Full feature set with all capabilities
- `debug` - Development builds with debugging enabled

**Channels:**
- `playstore` - Google Play Store version (with Firebase)
- `download` - Direct download version (with Firebase)
- `fdroid` - F-Droid version (no proprietary dependencies)

**Common Build Variants:**
- `slionsFullPlaystoreDebug` - Debug build for Play Store testing
- `slionsFullDownloadDebug` - Debug build for direct distribution
- `slionsFullFdroidDebug` - Debug build for F-Droid (no Firebase)
- `slionsFullPlaystoreRelease` - Production Play Store build
- `slionsFullDownloadRelease` - Production download build
- `slionsFullFdroidRelease` - Production F-Droid build

**Select Build Variant:**
- View → Tool Windows → Build Variants
- Choose the appropriate variant for your testing needs

## Key Features for Fulguris Development

### 1. Code Editor

**Kotlin Support:**
- Syntax highlighting and error detection
- Code completion and parameter hints
- Quick fixes and refactoring tools
- Navigate to declarations and usages

**Useful Shortcuts:**
- `Ctrl+Space` - Code completion
- `Ctrl+B` - Go to declaration
- `Ctrl+Alt+L` - Reformat code
- `Shift+F6` - Rename symbol
- `Ctrl+Shift+F` - Find in path
- `Alt+Enter` - Show intention actions and quick fixes

### 2. Layout Editor

**XML Layouts:**
- Visual design view with drag-and-drop components
- Split view (code + preview)
- Preview multiple screen sizes and orientations
- Dark mode preview

**Resource Management:**
- String resources editor with translation support
- Drawable and color resource management
- Style and theme editor

### 3. Debugging

**Breakpoint Debugging:**
1. **Set breakpoints** - Click gutter next to line number
2. **Start debug session** - Run → Debug 'app' or `Shift+F9`
3. **Step through code:**
   - `F8` - Step over
   - `F7` - Step into
   - `Shift+F8` - Step out
4. **Inspect variables** - Variables pane shows current values
5. **Evaluate expressions** - Watches and Evaluate Expression (Alt+F8)

**Logcat Integration:**
- View → Tool Windows → Logcat
- Filter by package name: `net.slions.fulguris`
- Filter by log level: Error, Warn, Info, Debug, Verbose
- Search logs with regex patterns

### 4. Profiling

**CPU Profiler:**
- Measure method execution time
- Identify performance bottlenecks
- Track thread activity

**Memory Profiler:**
- Monitor memory allocation
- Detect memory leaks
- Inspect heap dumps
- Force garbage collection

**Network Profiler:**
- Monitor HTTP/HTTPS requests
- View request/response headers and body
- Track network usage over time

**Battery Profiler:**
- Identify battery-draining operations
- Track wake locks and background activity
- Optimize for battery life

**How to Profile:**
1. Run → Profile 'app'
2. Select device and wait for app launch
3. Choose profiler type (CPU, Memory, Network, or Battery)
4. Interact with app to collect data
5. Analyze results in profiler view

### 5. Device Management

**Android Emulator:**
- Create virtual devices for testing
- Multiple device configurations (phone, tablet, foldable)
- Test different Android versions
- Simulate various screen sizes and densities

**Create Emulator:**
1. Tools → Device Manager
2. Click "Create Device"
3. Select hardware profile (e.g., Pixel 7)
4. Select system image (e.g., Android 14 API 34)
5. Configure AVD settings
6. Click "Finish"

**Physical Device Testing:**
1. Enable Developer Options on device
2. Enable USB Debugging
3. Connect via USB
4. Accept debugging authorization on device
5. Device appears in device dropdown

### 6. Build and Run

**Build Project:**
- Build → Make Project (`Ctrl+F9`)
- Build → Rebuild Project (clean + build)
- Build → Clean Project (remove build artifacts)

**Run Configurations:**
- Run → Edit Configurations
- Configure launch options, logcat filtering, profiling
- Set up multiple configurations for different testing scenarios

**Install and Run:**
- Run → Run 'app' (`Shift+F10`) - Install and launch
- Run → Apply Changes (`Ctrl+F10`) - Hot reload (when possible)
- Run → Apply Code Changes - Fast incremental updates

### 7. Version Control (Git)

**Git Integration:**
- View → Tool Windows → Git or Version Control
- Commit changes: `Ctrl+K`
- Push commits: `Ctrl+Shift+K`
- Pull changes: `Ctrl+T`
- View diff: `Ctrl+D`
- Resolve conflicts with visual merge tool

**Useful Git Features:**
- Annotate (Git Blame) - Right-click line → Git → Annotate
- Local History - View file history even before committing
- Changelists - Organize changes into logical groups
- Shelf - Temporarily save uncommitted changes

### 8. Code Analysis

**Inspections:**
- Analyze → Inspect Code
- Detects code issues, performance problems, security vulnerabilities
- Provides quick fixes for many issues

**Lint:**
- Analyze → Inspect Code (includes Lint)
- Android-specific code quality checks
- Resource optimization suggestions
- Accessibility recommendations

**TODO Management:**
- View → Tool Windows → TODO
- Lists all TODO, FIXME, and custom tags
- Filter by scope and pattern

## Common Development Tasks

### Task 1: Building Debug APK

```bash
# Via Gradle (recommended)
./gradlew assembleSlionsFullDownloadDebug

# Output location:
# app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk
```

**Via Android Studio:**
1. Build → Build Bundle(s) / APK(s) → Build APK(s)
2. Wait for build completion
3. Click "locate" in notification to open APK folder

### Task 2: Building Release APK

```bash
# Via Gradle
./gradlew assembleSlionsFullDownloadRelease

# Requires signing configuration in gradle.properties or keystore setup
```

**Note:** Release builds require proper signing configuration. See `app/build.gradle` for signing config.

### Task 3: Running Tests

**Unit Tests:**
```bash
# Via Gradle
./gradlew test

# Via Android Studio
# Right-click test class/method → Run 'TestName'
```

**Instrumented Tests:**
```bash
# Via Gradle
./gradlew connectedAndroidTest

# Via Android Studio
# Right-click test class/method → Run 'TestName'
```

### Task 4: Generating Signed Bundle for Play Store

```bash
# Via Gradle
./gradlew bundleSlionsFullPlaystoreRelease
```

**Via Android Studio:**
1. Build → Generate Signed Bundle / APK
2. Select "Android App Bundle"
3. Choose keystore and signing credentials
4. Select build variant: `slionsFullPlaystoreRelease`
5. Click "Finish"
6. Output: `app/build/outputs/bundle/slionsFullPlaystoreRelease/`

### Task 5: Analyzing APK Size

1. Build → Analyze APK
2. Select APK file from `app/build/outputs/apk/`
3. View breakdown:
   - DEX files (code)
   - Resources
   - Assets
   - Native libraries
4. Compare APK sizes between builds

### Task 6: Refactoring Code

**Rename Class/Method/Variable:**
- Right-click symbol → Refactor → Rename (`Shift+F6`)
- Enter new name
- Preview changes
- Click "Do Refactor"

**Extract Method:**
- Select code block
- Right-click → Refactor → Extract → Method (`Ctrl+Alt+M`)
- Name the method
- Click "OK"

**Move Class:**
- Right-click class → Refactor → Move (`F6`)
- Select target package
- Click "Refactor"

## Troubleshooting

### Gradle Sync Failed

**Symptom:** "Gradle sync failed" error on project open

**Solutions:**
1. **Check internet connection** - Gradle downloads dependencies
2. **Invalidate caches:** File → Invalidate Caches → Invalidate and Restart
3. **Update Gradle plugin:** Check `build.gradle` for latest version
4. **Clean project:** Build → Clean Project
5. **Check `local.properties`** - Ensure SDK path is correct
6. **Delete `.gradle` folder** - Let Gradle rebuild cache

### Emulator Not Starting

**Symptom:** Emulator fails to launch or crashes

**Solutions:**
1. **Check virtualization** - Enable Intel VT-x/AMD-V in BIOS
2. **Update emulator** - SDK Manager → Android Emulator (update)
3. **Increase RAM** - AVD Manager → Edit device → Increase RAM allocation
4. **Cold boot:** AVD Manager → Device → Cold Boot Now
5. **Wipe data:** AVD Manager → Device → Wipe Data

### Build Errors

**Symptom:** Compilation fails with errors

**Solutions:**
1. **Read error message** - Often points to exact issue
2. **Clean and rebuild:** Build → Clean Project, then Build → Rebuild Project
3. **Sync Gradle:** File → Sync Project with Gradle Files
4. **Update dependencies:** Check `gradle/libs.versions.toml` for version conflicts
5. **Check Java version:** File → Project Structure → SDK Location
6. **Invalidate caches:** File → Invalidate Caches → Invalidate and Restart

### Debugger Not Connecting

**Symptom:** Breakpoints not hit or debugger won't attach

**Solutions:**
1. **Check build variant** - Must be `Debug` variant
2. **Reinstall app** - Run → Run 'app' (fresh install)
3. **Check debuggable flag** - Ensure `debuggable true` in `build.gradle`
4. **Restart ADB:** Tools → Troubleshoot Device Connections
5. **Check device connection:** Unplug/replug USB cable

### Slow Performance

**Symptom:** Android Studio is slow or unresponsive

**Solutions:**
1. **Increase memory:** Help → Edit Custom VM Options
   ```
   -Xmx4096m
   -Xms1024m
   ```
2. **Disable unused plugins:** File → Settings → Plugins
3. **Exclude build folders:** File → Project Structure → Modules → Exclude build directories
4. **Update to latest version:** Check for updates
5. **Use Power Save Mode:** File → Power Save Mode (disables inspections)

## Tips and Best Practices

### Code Quality
- **Enable auto-formatting:** Settings → Editor → Code Style → Enable on save
- **Run inspections regularly:** Analyze → Inspect Code
- **Use TODO comments** for tracking pending work
- **Write unit tests** for critical business logic

### Performance
- **Use Apply Changes** (`Ctrl+F10`) instead of full reinstall when possible
- **Disable unnecessary inspections** for faster IDE performance
- **Use Gradle daemon** for faster builds (enabled by default)
- **Exclude large directories** (e.g., `node_modules`) from indexing

### Productivity
- **Learn keyboard shortcuts:** Help → Keyboard Shortcuts PDF
- **Use Live Templates:** Settings → Editor → Live Templates
- **Bookmarks:** `F11` to toggle, `Shift+F11` to view all
- **Recent Files:** `Ctrl+E` for quick navigation
- **Search Everywhere:** Double `Shift` for universal search

### Debugging
- **Use conditional breakpoints** - Right-click breakpoint → Condition
- **Log evaluation** - Right-click breakpoint → Evaluate and log
- **Enable exception breakpoints** - View Breakpoints → Java Exception Breakpoints
- **Attach debugger to running process** - Run → Attach Debugger to Android Process

## Plugins for Fulguris Development

**Recommended Plugins:**
- **Kotlin** (built-in) - Kotlin language support
- **.ignore** - Support for `.gitignore` and other ignore files
- **Rainbow Brackets** - Colorful bracket matching
- **Material Theme UI** - Alternative IDE theme
- **Translation** - Quick translation for string resources
- **SonarLint** - Additional code quality checks

**Install Plugins:**
- File → Settings → Plugins
- Search plugin name
- Click "Install"
- Restart IDE

## Related Documentation

- [App Manager](/Slion/Fulguris/wiki/App-Manager) - Device-side debugging and package management
- [BrowserLeaks](/Slion/Fulguris/wiki/browserleaks.com) - Web-based feature testing
- [Building](/Slion/Fulguris/wiki/Building) - Build configuration and release process

## External Links

- **Official Website:** https://developer.android.com/studio
- **Documentation:** https://developer.android.com/studio/intro
- **Release Notes:** https://developer.android.com/studio/releases
- **Keyboard Shortcuts:** https://developer.android.com/studio/intro/keyboard-shortcuts
- **Troubleshooting:** https://developer.android.com/studio/troubleshoot

---

**Last Updated:** December 21, 2025  
**Maintained by:** Fulguris Development Team
