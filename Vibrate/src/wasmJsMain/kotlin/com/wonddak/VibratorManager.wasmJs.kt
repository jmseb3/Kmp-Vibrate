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
    fun setTimeout(callback: () -> Unit, delay: Int)
}

external interface Navigator {
    fun vibrate(time: JsNumber): Boolean
    fun vibrate(times: JsArray<JsNumber>): Boolean
}

private fun checkVibrateSupport(): String =
    js("typeof window.navigator.vibrate === 'function'")

actual object VibratorManager {

    private fun isVibrateSupported(): Boolean {
        return if (checkVibrateSupport() == "true") {
            console.warn("Vibration API is not supported in this browser.")
            false
        } else {
            true
        }
    }

    actual fun vibrate(time: Long) {
        if (isVibrateSupported()) {
            window.navigator.vibrate(time.toInt().toJsNumber())
        }
    }

    actual fun vibratePattern(timings: List<Long>) {
        if (isVibrateSupported()) {
            val convertTimings = timings.toMutableList()
            if (convertTimings.isEmpty()) return

            val delayFirst = convertTimings.removeFirst().toInt()
            window.setTimeout({
                if (convertTimings.isNotEmpty()) {
                    val patternArray = JsArray<JsNumber>()

                    // Convert and add each Long to the JsArray
                    for (i in 0 until convertTimings.size) {
                        // The index operator internally calls a setter
                        patternArray[i] = convertTimings[i].toInt().toJsNumber()
                    }

                    window.navigator.vibrate(patternArray)
                }
            }, delayFirst)
        }
    }

    actual fun stopVibrate() {
        if (isVibrateSupported()) {
            window.navigator.vibrate(0.toJsNumber())
        }
    }
}