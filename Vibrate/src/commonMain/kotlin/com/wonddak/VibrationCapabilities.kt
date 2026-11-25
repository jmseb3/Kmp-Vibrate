package com.wonddak

/**
 * A snapshot of vibration features exposed by the current device or browser.
 * User settings and platform policies can still prevent playback.
 *
 * @property isSupported Matches [VibratorManager.isSupported]. On the web, this only
 * indicates that the browser exposes the Vibration API, not that hardware is present.
 * @property supportsAmplitudeControl Whether the library can apply nonzero strength
 * levels to vibration playback. Always false on JS and Wasm JS.
 */
data class VibrationCapabilities(
    val isSupported: Boolean,
    val supportsAmplitudeControl: Boolean
)
