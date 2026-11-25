package com.wonddak

import platform.CoreHaptics.CHHapticEngine
import platform.Foundation.NSThread
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType
import platform.UIKit.UISelectionFeedbackGenerator
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue


/**
 * Vibrator Manager
 */
actual object VibratorManager {

    private val customHaptic = CustomHaptic()

    actual fun isSupported(): Boolean {
        return runCatching {
            CHHapticEngine.capabilitiesForHardware().supportsHaptics
        }.getOrDefault(false)
    }

    actual fun capabilities(): VibrationCapabilities {
        val supported = isSupported()
        return VibrationCapabilities(
            isSupported = supported,
            supportsAmplitudeControl = supported
        )
    }

    actual fun feedback(type: HapticFeedback) = onMain {
        when (type) {
            HapticFeedback.SelectionChanged -> UISelectionFeedbackGenerator().selectionChanged()
            HapticFeedback.Click -> UIImpactFeedbackGenerator().impactOccurred()
            HapticFeedback.Success -> notifyFeedback(UINotificationFeedbackType.UINotificationFeedbackTypeSuccess)
            HapticFeedback.Warning -> notifyFeedback(UINotificationFeedbackType.UINotificationFeedbackTypeWarning)
            HapticFeedback.Error -> notifyFeedback(UINotificationFeedbackType.UINotificationFeedbackTypeError)
        }
    }

    private fun notifyFeedback(type: UINotificationFeedbackType) {
        UINotificationFeedbackGenerator().notificationOccurred(type)
    }

    actual fun vibrate(time: Long) = vibrate(time, 1f)

    actual fun vibrate(time: Long, strength: Float) {
        val duration = normalizeDurationMillis(time) ?: return
        val intensity = normalizeStrength(strength) ?: return
        play(listOf(Pulse(0L, duration, intensity)))
    }

    actual fun vibratePattern(timings: List<Long>) = vibratePattern(timings, 1f)

    actual fun vibratePattern(timings: List<Long>, strength: Float) {
        val validated = normalizePatternTimings(timings) ?: return
        val intensity = normalizeStrength(strength) ?: return
        play(List(validated.size / 2) { index ->
            Pulse(validated[index * 2], validated[index * 2 + 1], intensity)
        })
    }

    actual fun vibrateSegments(segments: List<VibrationSegment>, repeatCount: Int) {
        val pulses = preparePulses(segments, repeatCount) ?: return
        play(pulses)
    }

    private fun play(pulses: List<Pulse>) {
        if (!isSupported()) return
        dispatch_async(dispatch_get_main_queue()) {
            try {
                customHaptic.play(pulses)
            } catch (error: Exception) {
                println("Core Haptics playback failed: ${error.message}")
            }
        }
    }

    actual fun stopVibrate() {
        dispatch_async(dispatch_get_main_queue()) { customHaptic.stop() }
    }
}

private fun onMain(action: () -> Unit) {
    if (NSThread.isMainThread) action() else dispatch_async(dispatch_get_main_queue(), action)
}
