package com.wonddak

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreHaptics.CHHapticDynamicParameter
import platform.CoreHaptics.CHHapticEngine
import platform.CoreHaptics.CHHapticEngineFinishedActionStopEngine
import platform.CoreHaptics.CHHapticEngineStoppedReasonApplicationSuspended
import platform.CoreHaptics.CHHapticEngineStoppedReasonAudioSessionInterrupt
import platform.CoreHaptics.CHHapticEngineStoppedReasonIdleTimeout
import platform.CoreHaptics.CHHapticEngineStoppedReasonSystemError
import platform.CoreHaptics.CHHapticEvent
import platform.CoreHaptics.CHHapticEventParameter
import platform.CoreHaptics.CHHapticEventTypeHapticContinuous
import platform.CoreHaptics.CHHapticPattern
import platform.Foundation.NSTimeInterval


/**
 * Vibrator Manager
 */
actual class VibratorManager {

    private var customHaptic = CustomHaptic()

    private fun Long.toIosDuration(): NSTimeInterval {
        return this.toDouble() / 1000
    }

    /**
     * make vibrate for [time] second
     *
     * - 3000 = 3Sec
     */
    actual fun vibrate(time: Long) {
        try {
            customHaptic.playHaptic(
                listOf(
                    CHHapticEvent(
                        eventType = CHHapticEventTypeHapticContinuous,
                        parameters = emptyList<CHHapticEventParameter>(),
                        relativeTime = 0.1,
                        duration = time.toIosDuration()
                    )
                )
            )
        } catch (e: Exception) {
            println("vibrate error")
            e.printStackTrace()
        }
    }

    /**
     * make vibrate for [timings]
     * - 3000 = 3Sec
     * @param[timings] off/on Timing
     *
     * - if \[300,500,700,500] > 0.3 delay > 0.5 vibrate > 0.7 delay . 0.5 vibrate
     */
    actual fun vibratePattern(timings: LongArray) {
        try {
            val convertPattern = mutableListOf<CHHapticEvent>()
            var prevTime: Double? = null
            timings.forEachIndexed { index, time ->
                val convertDuration = time.toIosDuration()
                if (index % 2 == 0) {
                    prevTime = if (prevTime == null) {
                        convertDuration
                    } else {
                        prevTime!! + convertDuration
                    }
                } else {
                    CHHapticEvent(
                        eventType = CHHapticEventTypeHapticContinuous,
                        parameters = emptyList<CHHapticEventParameter>(),
                        relativeTime = prevTime!!,
                        duration = convertDuration
                    ).also {
                        convertPattern.add(it)
                    }
                    prevTime = prevTime!! + convertDuration
                }
            }
            customHaptic.playHaptic(convertPattern)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * stop Vibrate if running
     */
    actual fun stopVibrate() {
        customHaptic.stopHaptic()
    }
}

internal class CustomHaptic {
    private var engine: CHHapticEngine? = null

    @OptIn(ExperimentalForeignApi::class)
    @Throws(Throwable::class)
    internal fun playHaptic(
        eventPattern: List<CHHapticEvent>
    ) {
        if (engine == null) {
            resetEngine()
        }
        engine?.let { engine ->
            engine?.stopWithCompletionHandler {
                try {
                    val pattern = CHHapticPattern(
                        events = eventPattern,
                        parameters = emptyList<CHHapticDynamicParameter>(),
                        error = null
                    )
                    val player = engine.createPlayerWithPattern(pattern = pattern, error = null)
                    engine.notifyWhenPlayersFinished {
                        CHHapticEngineFinishedActionStopEngine
                    }
                    engine.startWithCompletionHandler {
                        player?.startAtTime(0.0, error = null)
                    }
                } catch (e: Exception) {
                    println("playHaptic error")
                    e.printStackTrace()
                }
            }
        }
    }

    internal fun stopHaptic() {
        engine?.stopWithCompletionHandler {

        }
    }


    @OptIn(ExperimentalForeignApi::class)
    private fun resetEngine() {
        try {
            engine = CHHapticEngine(null, null)
            engine?.setStoppedHandler { reason ->
                when (reason) {
                    CHHapticEngineStoppedReasonAudioSessionInterrupt -> {
                        println("REASON: Audio Session Interrupt")
                    }

                    CHHapticEngineStoppedReasonApplicationSuspended -> {
                        println("REASON: Application Suspended")
                    }

                    CHHapticEngineStoppedReasonIdleTimeout -> {
                        println("REASON: Idle Timeout")
                    }

                    CHHapticEngineStoppedReasonSystemError -> {
                        println("REASON: System Error")

                    }
                }
            }
        } catch (e: Exception) {
            println("reset error")
            e.printStackTrace()
        }
    }
}