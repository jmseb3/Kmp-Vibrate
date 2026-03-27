@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package com.wonddak

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

    private fun clearPendingPattern() {
        pendingTimeoutId?.let(window::clearTimeout)
        pendingTimeoutId = null
    }

    actual fun isSupported(): Boolean {
        return checkVibrateSupport() == "true"
    }

    actual fun vibrate(time: Long) {
        val safeDuration = normalizeDurationMillis(time) ?: return
        if (isSupported()) {
            clearPendingPattern()
            window.navigator.vibrate(safeDuration.toInt().toJsNumber())
        }
    }

    actual fun vibratePattern(timings: List<Long>) {
        if (!isSupported()) {
            return
        }

        val convertTimings = normalizePatternTimings(timings)?.toMutableList() ?: return
        clearPendingPattern()

        val delayFirst = convertTimings.removeFirst().toInt()
        val patternArray = JsArray<JsNumber>()
        for (i in 0 until convertTimings.size) {
            patternArray[i] = convertTimings[i].toInt().toJsNumber()
        }

        if (delayFirst == 0) {
            window.navigator.vibrate(patternArray)
            return
        }

        pendingTimeoutId = window.setTimeout({
            pendingTimeoutId = null
            window.navigator.vibrate(patternArray)
        }, delayFirst)
    }

    actual fun stopVibrate() {
        clearPendingPattern()
        if (isSupported()) {
            window.navigator.vibrate(0.toJsNumber())
        }
    }
}
