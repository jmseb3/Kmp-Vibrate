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
import platform.CoreHaptics.CHHapticEventParameterIDHapticIntensity
import platform.CoreHaptics.CHHapticEventTypeHapticContinuous
import platform.CoreHaptics.CHHapticPattern
import platform.Foundation.NSTimeInterval


/**
 * Vibrator Manager
 */
actual object VibratorManager {

    private var customHaptic = CustomHaptic()

    // Core Haptics works in seconds, while the shared API keeps timing values in milliseconds.
    private fun Long.toIosDuration(): NSTimeInterval {
        return this.toDouble() / 1000
    }

    actual fun isSupported(): Boolean {
        return runCatching {
            CHHapticEngine.capabilitiesForHardware().supportsHaptics
        }.getOrDefault(false)
    }

    /**
     * make vibrate for [time] second
     *
     * - 3000 = 3Sec
     */
    actual fun vibrate(time: Long) {
        vibrate(time, 1f)
    }

    actual fun vibrate(time: Long, strength: Float) {
        val safeDuration = normalizeDurationMillis(time) ?: return
        val eventParameters = strength.toIosEventParameters() ?: return
        if (!isSupported()) {
            return
        }
        try {
            customHaptic.playHaptic(
                listOf(
                    CHHapticEvent(
                        eventType = CHHapticEventTypeHapticContinuous,
                        parameters = eventParameters,
                        relativeTime = 0.0,
                        duration = safeDuration.toIosDuration()
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
    actual fun vibratePattern(timings: List<Long>) {
        vibratePattern(timings, 1f)
    }

    actual fun vibratePattern(timings: List<Long>, strength: Float) {
        if (!isSupported()) {
            return
        }

        val normalizedTimings = normalizePatternTimings(timings)?.toList() ?: return
        val eventParameters = strength.toIosEventParameters() ?: return
        try {
            val convertPattern = mutableListOf<CHHapticEvent>()
            var prevTime: Double? = null
            normalizedTimings.forEachIndexed { index, time ->
                val convertDuration = time.toIosDuration()
                if (index % 2 == 0) {
                    // The common API models patterns as delay/vibrate pairs, so we accumulate delays into relativeTime.
                    prevTime = if (prevTime == null) {
                        convertDuration
                    } else {
                        prevTime + convertDuration
                    }
                } else {
                    CHHapticEvent(
                        eventType = CHHapticEventTypeHapticContinuous,
                        // Reuse one normalized intensity value for every haptic segment in the pattern.
                        parameters = eventParameters,
                        relativeTime = prevTime!!,
                        duration = convertDuration
                    ).also {
                        convertPattern.add(it)
                    }
                    prevTime = prevTime + convertDuration
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

private fun Float.toIosEventParameters(): List<CHHapticEventParameter>? {
    val normalizedStrength = normalizeStrength(this) ?: return null
    return listOf(
        // Core Haptics exposes intensity as a normalized floating-point value.
        CHHapticEventParameter(
            parameterID = CHHapticEventParameterIDHapticIntensity,
            value = normalizedStrength
        )
    )
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
            // Stopping first makes a new request replace the previous pattern instead of overlapping it.
            engine.stopWithCompletionHandler {
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
            // iOS may stop or discard the engine after interruptions, so it is recreated lazily.
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
