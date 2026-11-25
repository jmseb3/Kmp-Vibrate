# Changelog

## 1.2.0

- Added `VibratorManager.capabilities()` to query vibration and amplitude-control support.
- Added UI feedback with UIKit effects on iOS, system click and tick effects on Android 29+, and timing presets as fallbacks.
- Added Kotlin Duration support, per-segment strength, and finite pattern repetition.
- Deprecated `VibratePattern`, its typed playback overloads, and `VibrationPreset.patterns`. Migrate to `VibrationSegment`, `vibrateSegments()`, and `VibrationPreset.segments`.
- Added capability details, a strength slider, feedback examples, and a pattern timeline with Kotlin code export to the sample.
- Made zero, negative, and NaN strength requests no-ops across targets, guarded web timing conversions, and restored iOS playback on the next request after engine interruptions or resets.

## 1.1.0

- Added configurable vibration strength to the shared API.
- Applied strength on Android through amplitude mapping and on iOS through Core Haptics intensity.
- Kept JS and Wasm API-compatible by ignoring strength where the browser vibration API has no amplitude control.
- Improved API safety with support checks and shared timing validation helpers.
- Expanded the sample app with preset demos, clearer pattern editing, and platform-specific sample modules.
- Added short UI feedback presets (`Click`, `DoubleClick`, `Tick`) and preset duration metadata.
- Refreshed README usage examples for presets, support checks, and installation coordinates.
