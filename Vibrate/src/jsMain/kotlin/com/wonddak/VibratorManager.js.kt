package com.wonddak

import kotlinx.browser.window

actual object VibratorManager {

    private var pendingTimeoutId: Int? = null

    // Keep the timeout handle so stop requests can cancel a delayed pattern before it starts.
    private fun clearPendingPattern() {
        pendingTimeoutId?.let(window::clearTimeout)
        pendingTimeoutId = null
    }

    actual fun isSupported(): Boolean {
        return window.navigator.asDynamic().vibrate != undefined
    }

    actual fun vibrate(time: Long) {
        val safeDuration = normalizeDurationMillis(time) ?: return
        if (isSupported()) {
            clearPendingPattern()
            window.navigator.vibrate(safeDuration)
        }
    }

    actual fun vibrate(time: Long, strength: Float) {
        // The Web Vibration API does not expose amplitude, so strength is intentionally ignored.
        vibrate(time)
    }

    actual fun vibratePattern(timings: List<Long>) {
        if (!isSupported()) {
            return
        }

        val convertTimings = normalizePatternTimings(timings)?.toMutableList() ?: return
        clearPendingPattern()

        // navigator.vibrate starts with "vibrate now", so an initial delay must be scheduled manually.
        val delayMillis = convertTimings.removeFirst().toInt()
        if (delayMillis == 0) {
            window.navigator.vibrate(convertTimings.toTypedArray())
            return
        }

        pendingTimeoutId = window.setTimeout({
            pendingTimeoutId = null
            window.navigator.vibrate(convertTimings.toTypedArray())
        }, delayMillis)
    }

    actual fun vibratePattern(timings: List<Long>, strength: Float) {
        // The Web Vibration API does not expose amplitude, so strength is intentionally ignored.
        vibratePattern(timings)
    }

    actual fun stopVibrate() {
        clearPendingPattern()
        if (isSupported()) {
            window.navigator.vibrate(0)
        }
    }
}
