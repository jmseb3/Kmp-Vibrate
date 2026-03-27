![](./readmeAsset/header.png)

# KMP Vibrate

Kotlin Multiplatform utility library for vibrate📱!

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.jmseb3/vibrate)

## How to Install

In `lib.versions.toml`  include this dependency version catalog

```toml
[versions]
vibrate = "1.0.0"

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
    implementation("io.github.jmseb3:vibrate:1.0.0")
}
```

## Sample App

Example app source lives in [`sample/`](sample).

- JS browser: `./gradlew :sample:webApp:jsBrowserDevelopmentRun --continue`
- WASM browser: `./gradlew :sample:webApp:wasmJsBrowserDevelopmentRun --continue`
- Android: run `sample:androidApp` from Android Studio
- iOS: open `sample/iosApp/iosApp.xcodeproj` in Xcode and run

## How To Use Vibrate

All durations are in milliseconds.

### Check Support

```kotlin
if (VibratorManager.isSupported()) {
    VibratorManager.vibrate(300)
}
```

### One Time

```kotlin
VibratorManager.vibrate(3000)
```

`3000` means 3 seconds.

### Pattern

```kotlin
VibratorManager.vibratePattern(listOf(300, 500, 700, 500))
```

`[300, 500, 700, 500]` means:

- wait `300ms`
- vibrate `500ms`
- wait `700ms`
- vibrate `500ms`

You can also use typed patterns:

```kotlin
VibratorManager.vibratePattern(
    listOf(
        VibratePattern(delay = 0, vibrate = 80),
        VibratePattern(delay = 60, vibrate = 120)
    )
)
```

### Presets

Built-in presets are available through `VibrationPreset`.

```kotlin
VibratorManager.vibrate(VibrationPreset.Success)
VibratorManager.vibrate(VibrationPreset.Error)
```

Available presets:

- `VibrationPreset.Success`
- `VibrationPreset.Warning`
- `VibrationPreset.Error`
- `VibrationPreset.Heartbeat`
- `VibrationPreset.Sos`

If you want to inspect or customize a preset, you can also read its pattern list:

```kotlin
val successPattern = VibrationPreset.Success.patterns
```

### Stop

```kotlin
VibratorManager.stopVibrate()
```


### In Kotlin/Js Or Kotlin/Wasm
This feature is supported by the Vibrate API.

For more detailed information, please refer to the official documentation:

[Vibrate Api](https://developer.mozilla.org/en-US/docs/Web/API/Vibration_API)
