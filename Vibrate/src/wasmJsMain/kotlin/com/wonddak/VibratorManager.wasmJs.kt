@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package com.wonddak

import com.wonddak.internal.normalizePatternTimings
import com.wonddak.internal.normalizeStrength
import com.wonddak.internal.preparePulses
import com.wonddak.internal.toTimings
import com.wonddak.model.VibrationSegment

@JsName("window")
external val window: Window

@JsName("console")
external val console: Console

external interface Console {
    fun warn(message: String)
}

external interface Window {
    val navigator: Navigator
    fun setTimeout(callback: () -> Unit, delay: Int): Int
    fun clearTimeout(handle: Int)
}

external interface Navigator {
    fun vibrate(time: JsAny): Boolean
}

private fun checkVibrateSupport(): String =
    js("String(typeof window !== 'undefined' && typeof window.navigator !== 'undefined' && typeof window.navigator.vibrate === 'function')")

actual object VibratorManager {

    private var pendingTimeoutId: Int? = null

    // Keep the timeout handle so stop requests can cancel a delayed pattern before it starts.
    private fun clearPendingPattern() {
        pendingTimeoutId?.let(window::clearTimeout)
        pendingTimeoutId = null
    }

    actual fun isSupported(): Boolean {
        return checkVibrateSupport() == "true"
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
            window.navigator.vibrate(time.toInt().toJsNumber())
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
        val delayFirst = convertTimings[0].toInt()
        val patternArray = JsArray<JsNumber>()
        for (i in 1 until convertTimings.size) {
            patternArray[i - 1] = convertTimings[i].toInt().toJsNumber()
        }

        if (delayFirst == 0) {
            window.navigator.vibrate(patternArray)
            return
        }

        window.navigator.vibrate(0.toJsNumber())
        pendingTimeoutId = window.setTimeout({
            pendingTimeoutId = null
            window.navigator.vibrate(patternArray)
        }, delayFirst)
    }

    actual fun vibratePattern(timings: List<Long>, strength: Float) {
        // The Web Vibration API does not expose amplitude, so strength is intentionally ignored.
        if (normalizeStrength(strength) == null) return
        vibratePattern(timings)
    }

    actual fun stopVibrate() {
        clearPendingPattern()
        if (isSupported()) {
            window.navigator.vibrate(0.toJsNumber())
        }
    }
}
