package com.wonddak

import kotlinx.browser.window

actual object VibratorManager {
    private fun isVibrateSupported(): Boolean {
        return if (window.navigator.asDynamic().vibrate == undefined) {
            console.warn("Vibration API is not supported in this browser.")
            false
        } else {
            true
        }
    }

    actual fun vibrate(time: Long) {
        if (isVibrateSupported()) {
            window.navigator.vibrate(time)
        }
    }

    actual fun vibratePattern(timings: List<Long>) {
        if (isVibrateSupported()) {
            val convertTimings = timings.toMutableList()
            val delaySecond = convertTimings.removeFirst().toInt()
            window.setTimeout({
                window.navigator.vibrate(convertTimings.toTypedArray())
            }, delaySecond)
        }
    }

    actual fun stopVibrate() {
        if (isVibrateSupported()) {
            window.navigator.vibrate(0)
        }
    }
}