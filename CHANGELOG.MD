# Changelog

## 1.1.0

- Added configurable vibration strength to the shared API.
- Applied strength on Android through amplitude mapping and on iOS through Core Haptics intensity.
- Kept JS and Wasm API-compatible by ignoring strength where the browser vibration API has no amplitude control.
- Improved API safety with support checks and shared timing validation helpers.
- Expanded the sample app with preset demos, clearer pattern editing, and platform-specific sample modules.
- Added short UI feedback presets (`Click`, `DoubleClick`, `Tick`) and preset duration metadata.
- Refreshed README usage examples for presets, support checks, and beta installation coordinates.
