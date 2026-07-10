# AGENTS.md

This file is for agents working in this repository. Keep changes small, verify the
actual target you touched, and avoid broad refactors unless the task explicitly
asks for them.

## Project Overview

KMP-Vibrate is a Kotlin Multiplatform vibration and haptics library.

- Library module: `:Vibrate`
- Sample shared UI: `:sample:sharedUI`
- Android sample app: `:sample:androidApp`
- Web sample app: `:sample:webApp`
- iOS sample project: `sample/iosApp/iosApp.xcodeproj`

The public Maven coordinates are controlled by `Vibrate/gradle.properties`:

- `GROUP=io.github.jmseb3`
- `POM_ARTIFACT_ID=vibrate`
- `VERSION_NAME=...`

## Working Rules

- Think before editing. State assumptions when a change depends on platform
  behavior or publishing setup.
- Prefer simple, surgical changes. Do not add abstractions, features, or error
  handling that were not requested.
- Preserve the public API unless the task explicitly asks for an API change.
- Do not revert unrelated local changes.
- Keep sample updates aligned with the library API.
- Use ASCII in new files unless existing project content requires otherwise.
- Use `rtk` before shell commands in this repository.

## Important Source Paths

- Common API: `Vibrate/src/commonMain/kotlin/com/wonddak/VibratorManager.kt`
- Presets: `Vibrate/src/commonMain/kotlin/com/wonddak/VibrationPreset.kt`
- Pattern model: `Vibrate/src/commonMain/kotlin/com/wonddak/model/VibratePattern.kt`
- Android implementation: `Vibrate/src/androidMain/kotlin/com/wonddak/VibratorManager.android.kt`
- iOS implementation: `Vibrate/src/iosMain/kotlin/com/wonddak/VibratorManager.ios.kt`
- JS implementation: `Vibrate/src/jsMain/kotlin/com/wonddak/VibratorManager.js.kt`
- Wasm JS implementation: `Vibrate/src/wasmJsMain/kotlin/com/wonddak/VibratorManager.wasmJs.kt`
- Shared validation tests: `Vibrate/src/commonTest/kotlin/com/wonddak/VibratorManagerValidationTest.kt`
- Sample Compose UI: `sample/sharedUI/src/commonMain/kotlin/dev/wonddak/vibrateExample/`

## Validation Commands

Use the narrowest command that covers your change:

```bash
rtk ./gradlew --no-daemon :Vibrate:allTests
rtk ./gradlew --no-daemon :sample:sharedUI:compileCommonMainKotlinMetadata
```

For release readiness, also verify local publication:

```bash
rtk ./gradlew --no-daemon :Vibrate:publishToMavenLocal
```

Sample app commands from the README:

```bash
rtk ./gradlew :sample:webApp:jsBrowserDevelopmentRun --continue
rtk ./gradlew :sample:webApp:wasmJsBrowserDevelopmentRun --continue
```

## Release Checklist

Before preparing a release:

1. Update `VERSION_NAME` in `Vibrate/gradle.properties`.
2. Update README installation examples.
3. Update `CHANGELOG.MD`.
4. Search for stale version suffixes such as `beta` or `SNAPSHOT`.
5. Run `:Vibrate:allTests`.
6. Run `:Vibrate:publishToMavenLocal`.

Do not publish to Maven Central unless the user explicitly asks for remote
publishing and the required signing and Maven Central credentials are available.

## Platform Notes

- Android maps `strength` to `VibrationEffect` amplitude on API 26+.
- iOS maps `strength` to Core Haptics intensity.
- JS and Wasm JS intentionally ignore `strength` because the browser Vibration
  API only supports timing patterns.
- Pattern timings use `[delay, vibrate, delay, vibrate]`; odd indexes are
  vibration windows and must be positive.

## Git Conventions

- Commit completed work without delay after finishing the requested change.
- Follow the repository commit rules before committing.
- Keep commits focused enough to describe in one sentence.
- Use English Conventional Commit messages, for example:
  - `feat: add vibration presets`
  - `fix: validate pattern timings`
  - `chore: prepare 1.1.0 release`
