package com.wonddak

import kotlinx.browser.window
import kotlin.js.jsTypeOf

actual object VibratorManager {

    private var pendingTimeoutId: Int? = null

    // Keep the timeout handle so stop requests can cancel a delayed pattern before it starts.
    private fun clearPendingPattern() {
        pendingTimeoutId?.let(window::clearTimeout)
        pendingTimeoutId = null
    }

    actual fun isSupported(): Boolean {
        return jsTypeOf(window.navigator.asDynamic().vibrate) == "function"
    }

    actual fun capabilities(): VibrationCapabilities = VibrationCapabilities(
        isSupported = isSupported(),
        supportsAmplitudeControl = false
    )

    actual fun feedback(type: HapticFeedback) = vibrate(type.fallback)

    actual fun vibrateSegments(segments: List<VibrationSegment>, repeatCount: Int) {
        val pulses = preparePulses(segments, repeatCount) ?: return
        vibratePattern(pulses.toTimings())
    }

    actual fun vibrate(time: Long) {
        if (time !in 1..Int.MAX_VALUE.toLong()) return
        if (isSupported()) {
            clearPendingPattern()
            window.navigator.vibrate(time.toInt())
        }
    }

    actual fun vibrate(time: Long, strength: Float) {
        // The Web Vibration API does not expose amplitude, so strength is intentionally ignored.
        if (normalizeStrength(strength) == null) return
        vibrate(time)
    }

    actual fun vibratePattern(timings: List<Long>) {
        if (!isSupported()) {
            return
        }

        val convertTimings = normalizePatternTimings(timings) ?: return
        if (convertTimings.any { it > Int.MAX_VALUE }) return
        clearPendingPattern()

        // navigator.vibrate starts with "vibrate now", so an initial delay must be scheduled manually.
        val delayMillis = convertTimings[0].toInt()
        val pattern = Array(convertTimings.size - 1) { convertTimings[it + 1].toInt() }
        if (delayMillis == 0) {
            window.navigator.asDynamic().vibrate(pattern)
            return
        }

        window.navigator.vibrate(0)
        pendingTimeoutId = window.setTimeout({
            pendingTimeoutId = null
            window.navigator.asDynamic().vibrate(pattern)
        }, delayMillis)
    }

    actual fun vibratePattern(timings: List<Long>, strength: Float) {
        // The Web Vibration API does not expose amplitude, so strength is intentionally ignored.
        if (normalizeStrength(strength) == null) return
        vibratePattern(timings)
    }

    actual fun stopVibrate() {
        clearPendingPattern()
        if (isSupported()) {
            window.navigator.vibrate(0)
        }
    }
}
