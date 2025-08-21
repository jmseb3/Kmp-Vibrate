![](./readmeAsset/header.png)

# KMP Vibrate

Kotlin Multiplatform utility library for vibrate📱!

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.jmseb3/vibrate)

## How to Install

In `lib.versions.toml`  include this dependency version catalog

```toml
[versions]
vibrate = "1.0.0-alpha01"

[libraries]
vibrate = { module = "io.github.jmseb3:vibrate", version.ref = "vibrate" }
```

```kotlin
commonMain.dependencies {
    implementation(libs.vibrate)
}
```

or In `build.gradle` of app module, include this dependency

```gradle
commonMain.dependencies {
    implementation("io.github.jmseb3:vibrate:1.0.0-alpha01")
}
```

<details>
<summary>How To Test sample?</summary>

### Android

To run the application on android device/emulator:

- open project in Android Studio and run imported android run configuration

### iOS

To run the application on iPhone device/simulator:

- Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration

### JS Browser

Run the browser application: `./gradlew :sample:composeApp:jsBrowserDevelopmentRun --continue`

### Wasm Browser

Run the browser application: `./gradlew :sample:composeApp:wasmJsBrowserDevelopmentRun --continue`
</details>

---

## How To Use Vibrate
use `VibratorManager` 

### one Time

use `VibratorManager.vibrate(time:Long)`

if time 3000 = 3 Second

### Pattern

use `VibratorManager.vibratePattern(timings:List<Long>)`

if timings is [300,500,700,500]
> 0.3 delay > 0.5 vibrate > 0.7 delay > 0.5 vibrate

### Stop

`VibratorManager.stopVibrate()`


### In Kotlin/Js Or Kotlin/Wasm
This feature is supported by the Vibrate API.

For more detailed information, please refer to the official documentation:

[Vibrate Api](https://developer.mozilla.org/en-US/docs/Web/API/Vibration_API)