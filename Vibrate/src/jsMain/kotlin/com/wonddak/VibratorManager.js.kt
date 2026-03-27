package com.wonddak

import kotlinx.browser.window

actual object VibratorManager {

    private var pendingTimeoutId: Int? = null

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

    actual fun vibratePattern(timings: List<Long>) {
        if (!isSupported()) {
            return
        }

        val convertTimings = normalizePatternTimings(timings)?.toMutableList() ?: return
        clearPendingPattern()

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

    actual fun stopVibrate() {
        clearPendingPattern()
        if (isSupported()) {
            window.navigator.vibrate(0)
        }
    }
}
