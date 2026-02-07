# Gradle Wrapper (gradlew)

## Overview

**Gradle Wrapper** (`gradlew` / `gradlew.bat`) is a script that invokes a declared version of Gradle, ensuring consistent builds across different environments. It's the recommended way to build Fulguris, as it guarantees all developers and CI systems use the same Gradle version.

**Documentation:** https://docs.gradle.org/current/userguide/gradle_wrapper.html
**License:** Apache License 2.0

## Why Gradle Wrapper for Fulguris

The Gradle Wrapper solves version consistency problems in Android development:

- **Version Consistency:** All developers use the same Gradle version
- **No Installation Required:** Wrapper downloads Gradle automatically on first use
- **CI/CD Friendly:** Build servers don't need pre-installed Gradle
- **Project-Specific:** Each project can specify its own Gradle version
- **Reproducible Builds:** Eliminates "works on my machine" issues
- **Easy Updates:** Update entire team's Gradle version with one commit

## Gradle vs Gradle Wrapper

| Aspect | `gradle` (System) | `gradlew` (Wrapper) |
|--------|-------------------|---------------------|
| Installation | Manual (system-wide) | Automatic (project-specific) |
| Version | Whatever is installed | Specified in `gradle-wrapper.properties` |
| Consistency | Varies by machine | Guaranteed across team |
| Recommendation | ❌ Not recommended | ✅ Always use for projects |

**Always use `./gradlew` for Fulguris, not `gradle`!**

## Location in Fulguris Project

```
Fulguris/
├── gradlew              # Unix/Linux/macOS script
├── gradlew.bat          # Windows batch script
└── gradle/
    └── wrapper/
        ├── gradle-wrapper.jar           # Wrapper executable
        └── gradle-wrapper.properties    # Gradle version config
```

**Configuration File:** `gradle/wrapper/gradle-wrapper.properties`
```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

## Usage

### Basic Syntax

**Windows (PowerShell/CMD):**
```powershell
.\gradlew <task> [options]
```

**Unix/Linux/macOS:**
```bash
./gradlew <task> [options]
```

### First Run

On first execution, Gradle Wrapper downloads the specified Gradle version:

```powershell
.\gradlew tasks

# Output:
# Downloading https://services.gradle.org/distributions/gradle-8.5-bin.zip
# .......10%.......20%.......30%.......40%.......50%.......60%.......70%.......80%.......90%.......100%
# Unzipping C:\Users\{user}\.gradle\wrapper\dists\gradle-8.5-bin\...
```

**Download Location:**
- Windows: `C:\Users\{username}\.gradle\wrapper\dists\`
- macOS/Linux: `~/.gradle/wrapper/dists/`

## Essential Gradle Tasks for Fulguris

### Building

```powershell
# List all available tasks
.\gradlew tasks

# List all tasks including subtasks
.\gradlew tasks --all

# Clean build artifacts
.\gradlew clean

# Build debug APK
.\gradlew assembleDebug

# Build release APK (requires signing config)
.\gradlew assembleRelease

# Build specific variant
.\gradlew assembleSlionsFullDownloadDebug
.\gradlew assembleSlionsFullDownloadRelease
.\gradlew assembleSlionsFullPlaystoreDebug
.\gradlew assembleSlionsFullFdroidRelease

# Build all variants
.\gradlew assemble
```

**APK Output Location:**
```
app/build/outputs/apk/{flavor}/{buildType}/app-{flavor}-{buildType}.apk

Examples:
app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk
app/build/outputs/apk/slionsFullPlaystore/release/app-slions-full-playstore-release.apk
```

### Building Android App Bundles

```powershell
# Build bundle for Play Store (debug)
.\gradlew bundleSlionsFullPlaystoreDebug

# Build bundle for Play Store (release)
.\gradlew bundleSlionsFullPlaystoreRelease

# Build all bundles
.\gradlew bundle
```

**Bundle Output Location:**
```
app/build/outputs/bundle/{flavor}{BuildType}/app-{flavor}-{buildType}.aab

Example:
app/build/outputs/bundle/slionsFullPlaystoreRelease/app-slions-full-playstore-release.aab
```

### Installing

```powershell
# Build and install debug variant on connected device
.\gradlew installDebug

# Install specific variant
.\gradlew installSlionsFullDownloadDebug

# Install and launch
.\gradlew installSlionsFullDownloadDebug
adb shell am start -n net.slions.fulguris.full.download/.BrowserActivity

# Uninstall from device
.\gradlew uninstallDebug
.\gradlew uninstallSlionsFullDownloadDebug
```

### Testing

```powershell
# Run unit tests
.\gradlew test

# Run unit tests for specific variant
.\gradlew testSlionsFullDownloadDebugUnitTest

# Run instrumented tests (requires connected device/emulator)
.\gradlew connectedAndroidTest

# Run instrumented tests for specific variant
.\gradlew connectedSlionsFullDownloadDebugAndroidTest

# Generate test coverage report
.\gradlew testSlionsFullDownloadDebugUnitTestCoverage

# Run lint checks
.\gradlew lint

# Generate lint report (HTML)
.\gradlew lintSlionsFullDownloadDebug
```

**Test Reports Location:**
```
app/build/reports/tests/testSlionsFullDownloadDebugUnitTest/index.html
app/build/reports/androidTests/connected/index.html
app/build/reports/lint-results.html
```

### Code Quality

```powershell
# Run lint analysis
.\gradlew lint

# Lint with warnings as errors
.\gradlew lint -PwarningsAsErrors=true

# Generate dependency report
.\gradlew dependencies

# Check for dependency updates
.\gradlew dependencyUpdates

# Generate build scan (requires --scan flag)
.\gradlew build --scan
```

### Project Information

```powershell
# Show project structure
.\gradlew projects

# Show project dependencies
.\gradlew app:dependencies

# Show build configuration
.\gradlew properties

# Show Android SDK location
.\gradlew -q app:properties | Select-String "android.sdkDirectory"

# Show Gradle version
.\gradlew --version
```

## Common Build Workflows

### Workflow 1: Clean Build Debug APK

```powershell
# Clean, build, and install debug variant
.\gradlew clean assembleSlionsFullDownloadDebug
adb install -r app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk
```

**When to Use:**
- Fresh build after major changes
- Resolving build cache issues
- Before releases

### Workflow 2: Quick Incremental Build

```powershell
# Build without clean (faster)
.\gradlew assembleSlionsFullDownloadDebug
```

**When to Use:**
- During active development
- Quick testing iterations
- Most day-to-day builds

### Workflow 3: Full Test Suite

```powershell
# Clean, build, test, lint
.\gradlew clean build

# This runs:
# - Unit tests
# - Lint checks
# - Assembles all variants
```

**When to Use:**
- Before committing code
- Pre-merge validation
- Release preparation

### Workflow 4: Build Release Bundle

```powershell
# Build signed release bundle for Play Store
.\gradlew clean bundleSlionsFullPlaystoreRelease

# Output:
# app/build/outputs/bundle/slionsFullPlaystoreRelease/app-slions-full-playstore-release.aab
```

**When to Use:**
- Publishing to Google Play
- Production releases

### Workflow 5: Generate All Variants

```powershell
# Build all flavors and build types
.\gradlew assemble

# Outputs:
# - slionsFullDownloadDebug
# - slionsFullDownloadRelease
# - slionsFullPlaystoreDebug
# - slionsFullPlaystoreRelease
# - slionsFullFdroidDebug
# - slionsFullFdroidRelease
# - (and styx variants)
```

**When to Use:**
- Release preparation
- Testing all distributions
- CI/CD full builds

## Advanced Gradle Commands

### Build Options

```powershell
# Parallel builds (faster on multi-core)
.\gradlew assemble --parallel

# Offline mode (use cached dependencies)
.\gradlew build --offline

# Refresh dependencies (force re-download)
.\gradlew build --refresh-dependencies

# Continue build despite failures
.\gradlew build --continue

# Show stacktrace on errors
.\gradlew build --stacktrace

# Full debug output
.\gradlew build --debug

# Profile build performance
.\gradlew build --profile
# Report: build/reports/profile/
```

### Gradle Daemon

The Gradle Daemon speeds up builds by keeping Gradle in memory:

```powershell
# Check daemon status
.\gradlew --status

# Stop daemon
.\gradlew --stop

# Daemon is enabled by default
# Configure in gradle.properties:
# org.gradle.daemon=true
```

**Note:** On Windows, the daemon keeps files locked. If you encounter "access denied" or "could not delete" errors during builds, stop the daemon with `.\gradlew --stop` to release file locks, then rebuild.

### Build Cache

```powershell
# Enable build cache (faster repeated builds)
# In gradle.properties:
# org.gradle.caching=true

# Clean build cache
Remove-Item -Recurse -Force $env:GRADLE_USER_HOME\caches\

# Windows default: C:\Users\{user}\.gradle\caches\
```

### Custom Properties

Pass properties to build:

```powershell
# Define property
.\gradlew assembleRelease -PversionCode=123

# Access in build.gradle:
# android {
#     defaultConfig {
#         versionCode project.hasProperty('versionCode') ? versionCode.toInteger() : 1
#     }
# }
```

## Build Configuration Files

### gradle.properties

Project-wide Gradle settings:

```properties
# Enable AndroidX
android.useAndroidX=true

# Enable Jetifier (migrate support libs)
android.enableJetifier=true

# Gradle daemon
org.gradle.daemon=true

# Parallel builds
org.gradle.parallel=true

# Build cache
org.gradle.caching=true

# JVM memory settings
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError

# Kotlin compiler options
kotlin.code.style=official
```

### settings.gradle

Project structure configuration:

```groovy
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Fulguris"
include ':app'
include ':subs:AppIntro:appintro'
include ':subs:Preference:lib'
```

### build.gradle (Project-level)

Top-level build configuration:

```groovy
buildscript {
    ext.kotlin_version = '1.9.21'
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.2.0'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

### build.gradle (App-level)

Module-specific build configuration:

```groovy
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
}

android {
    compileSdk 34

    defaultConfig {
        applicationId "net.slions.fulguris.full.download"
        minSdk 26
        targetSdk 34
        versionCode 252
        versionName "1.9.8"
    }

    buildTypes {
        debug {
            debuggable true
        }
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    flavorDimensions "branding", "distribution"
    productFlavors {
        slions { dimension "branding" }
        styx { dimension "branding" }

        fullDownload { dimension "distribution" }
        fullPlaystore { dimension "distribution" }
        fullFdroid { dimension "distribution" }
    }
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
}
```

## Troubleshooting

### Build Failed: "SDK location not found"

**Symptom:** `SDK location not found. Define location with sdk.dir in the local.properties file or with an ANDROID_HOME environment variable.`

**Solution:**

Create `local.properties` in project root:
```properties
sdk.dir=C\:\\Users\\{YourUsername}\\AppData\\Local\\Android\\Sdk
```

Or set environment variable:
```powershell
$env:ANDROID_HOME = "C:\Users\$env:USERNAME\AppData\Local\Android\Sdk"
```

### Build Failed: "Unable to start the daemon process"

**Symptom:** Gradle daemon fails to start

**Solutions:**
1. **Increase memory** in `gradle.properties`:
   ```properties
   org.gradle.jvmargs=-Xmx4096m
   ```
2. **Stop existing daemon:**
   ```powershell
   .\gradlew --stop
   ```
3. **Clear Gradle cache:**
   ```powershell
   Remove-Item -Recurse -Force $env:USERPROFILE\.gradle\caches\
   ```

### Build Failed: "Could not resolve dependencies"

**Symptom:** Dependencies fail to download

**Solutions:**
1. **Check internet connection**
2. **Refresh dependencies:**
   ```powershell
   .\gradlew build --refresh-dependencies
   ```
3. **Try offline mode if dependencies cached:**
   ```powershell
   .\gradlew build --offline
   ```
4. **Clear dependency cache:**
   ```powershell
   Remove-Item -Recurse -Force $env:USERPROFILE\.gradle\caches\modules-2\
   ```

### Build Failed: "Execution failed for task ':app:processDebugResources'"

**Symptom:** Resource processing fails

**Solutions:**
1. **Clean and rebuild:**
   ```powershell
   .\gradlew clean build
   ```
2. **Check for duplicate resources** in XML files
3. **Invalidate Android Studio cache:**
   - File → Invalidate Caches → Invalidate and Restart
4. **Update Gradle and Android Gradle Plugin** to latest versions

### Build Very Slow

**Symptom:** Builds take excessive time

**Solutions:**
1. **Enable parallel builds** in `gradle.properties`:
   ```properties
   org.gradle.parallel=true
   org.gradle.caching=true
   ```
2. **Increase JVM memory:**
   ```properties
   org.gradle.jvmargs=-Xmx4096m
   ```
3. **Use build cache:**
   ```properties
   org.gradle.caching=true
   ```
4. **Disable unused build variants** in Android Studio:
   - Build → Select Build Variant → Choose one variant
5. **Use incremental builds** (skip `clean` unless necessary)

### Gradle Version Mismatch

**Symptom:** `The project is using an incompatible version (AGP 8.2.0) of the Android Gradle plugin`

**Solution:**

Update `gradle-wrapper.properties`:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
```

Then sync:
```powershell
.\gradlew wrapper --gradle-version=8.5
```

### Permission Denied (Unix/Linux/macOS)

**Symptom:** `bash: ./gradlew: Permission denied`

**Solution:**
```bash
chmod +x gradlew
./gradlew tasks
```

## Performance Optimization

### gradle.properties Tuning

Recommended settings for faster builds:

```properties
# Enable parallel execution
org.gradle.parallel=true

# Enable caching
org.gradle.caching=true

# Enable configuration on demand
org.gradle.configureondemand=true

# Increase memory (adjust based on available RAM)
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8

# Enable daemon (default)
org.gradle.daemon=true

# Use AndroidX
android.useAndroidX=true

# Enable R8 full mode (faster, better optimization)
android.enableR8.fullMode=true

# Enable non-transitive R classes
android.nonTransitiveRClass=true

# Kotlin compilation options
kotlin.incremental=true
kotlin.incremental.js=true
kapt.incremental.apt=true
```

### Build Profiling

Identify slow tasks:

```powershell
# Generate build profile
.\gradlew build --profile

# Report location:
# build/reports/profile/profile-{timestamp}.html
```

**Analyze:**
- Configuration time
- Task execution time
- Dependency resolution time

### Incremental Builds

Gradle skips up-to-date tasks:

```powershell
# First build (all tasks run)
.\gradlew assembleDebug

# Make small code change

# Second build (only affected tasks run)
.\gradlew assembleDebug
# UP-TO-DATE appears for unchanged tasks
```

## Tips and Best Practices

### Always Use Wrapper

```powershell
# ✅ CORRECT - Use wrapper
.\gradlew build

# ❌ WRONG - Don't use system Gradle
gradle build
```

### Commit Wrapper Files

Ensure these are in Git:
- ✅ `gradlew`
- ✅ `gradlew.bat`
- ✅ `gradle/wrapper/gradle-wrapper.jar`
- ✅ `gradle/wrapper/gradle-wrapper.properties`

### Keep Gradle Updated

Update wrapper to latest version:

```powershell
# Update to specific version
.\gradlew wrapper --gradle-version=8.5

# Update to latest release
.\gradlew wrapper --gradle-version=latest
```

### Use Build Scans

Get detailed build insights:

```powershell
.\gradlew build --scan

# Gradle generates URL like:
# https://gradle.com/s/abcde12345
# Share with team to debug build issues
```

### Avoid Clean Unless Necessary

```powershell
# ✅ FAST - Incremental build
.\gradlew assembleDebug

# ❌ SLOW - Clean build (only when needed)
.\gradlew clean assembleDebug
```

**When to clean:**
- Major refactoring
- Resource changes not picked up
- Strange build errors
- Before release builds

## Integration with Development Workflow

### With ADB

```powershell
# Build and install in one command
.\gradlew installSlionsFullDownloadDebug

# Or build first, then install
.\gradlew assembleSlionsFullDownloadDebug
adb install -r app/build/outputs/apk/slionsFullDownload/debug/app-slions-full-download-debug.apk
```

### With Android Studio

Android Studio uses Gradle wrapper internally:
- **Build → Make Project** → Runs `.\gradlew assemble`
- **Build → Clean Project** → Runs `.\gradlew clean`
- **Run → Run 'app'** → Runs `.\gradlew installDebug`

**View Gradle tasks:**
- View → Tool Windows → Gradle
- Browse tasks by module and type

### With CI/CD

```yaml
# GitHub Actions example
- name: Build Debug APK
  run: ./gradlew assembleSlionsFullDownloadDebug

- name: Run Tests
  run: ./gradlew test

- name: Upload APK
  uses: actions/upload-artifact@v3
  with:
    name: debug-apk
    path: app/build/outputs/apk/slionsFullDownload/debug/*.apk
```

## Related Documentation

- [ADB](/Slion/Fulguris/wiki/ADB) - Installing and launching APKs on devices
- [Android Studio](/Slion/Fulguris/wiki/Android-Studio) - IDE integration with Gradle
- [Building](/Slion/Fulguris/wiki/Building) - Complete build process
- [Testing](/Slion/Fulguris/wiki/Testing) - Running tests with Gradle

## External Links

- **Gradle Wrapper Documentation:** https://docs.gradle.org/current/userguide/gradle_wrapper.html
- **Gradle User Guide:** https://docs.gradle.org/current/userguide/userguide.html
- **Android Gradle Plugin:** https://developer.android.com/build
- **Build Performance:** https://developer.android.com/build/optimize-your-build
- **Gradle Build Scans:** https://scans.gradle.com/

---

**Last Updated:** December 21, 2025
**Maintained by:** Fulguris Development Team
