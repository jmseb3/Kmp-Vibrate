package com.wonddak

import kotlinx.browser.window

actual object VibratorManager {
    private fun isVibrateSupported(): Boolean {
        return if (window.asDynamic().navigator.vibrate == undefined) {
            console.warn("Vibration API is not supported in this browser.")
            false
        } else {
            true
        }
    }

    actual fun vibrate(time: Long) {
        if (isVibrateSupported()) {
            window.asDynamic().navigator.vibrate(time)
        }
    }

    actual fun vibratePattern(timings: List<Long>) {
        if (isVibrateSupported()) {
            val convertTimings = timings.toMutableList()
            val delaySecond = convertTimings.removeFirst().toInt()
            window.asDynamic().setTimeout({
                window.asDynamic().navigator.vibrate(convertTimings.toTypedArray())
            }, delaySecond)
        }
    }

    actual fun stopVibrate() {
        if (isVibrateSupported()) {
            window.asDynamic().navigator.vibrate(0)
        }
    }
}