package com.wonddak

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.wonddak.internal.normalizeDurationMillis
import com.wonddak.internal.normalizePatternTimings
import com.wonddak.internal.normalizeStrength
import com.wonddak.internal.preparePulses
import com.wonddak.internal.toTimings
import com.wonddak.model.VibrationSegment
import kotlin.math.roundToInt

/**
 * Vibrator Manager
 */
@Suppress("DEPRECATION")
actual object VibratorManager {

    private lateinit var vibrator: Vibrator

    // AndroidX Startup initializes this in normal app launches, but tests and host apps can still
    // hit the API before setup has happened.
    private fun vibratorOrNull(): Vibrator? {
        if (!::vibrator.isInitialized) {
            return null
        }
        return vibrator.takeIf { it.hasVibrator() }
    }

    fun initializer(context: Context): VibratorManager {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        return VibratorManager
    }

    actual fun isSupported(): Boolean {
        return vibratorOrNull() != null
    }

    actual fun capabilities(): VibrationCapabilities {
        val currentVibrator = vibratorOrNull()
        return VibrationCapabilities(
            isSupported = currentVibrator != null,
            supportsAmplitudeControl = currentVibrator != null &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && currentVibrator.hasAmplitudeControl()
        )
    }

    actual fun feedback(type: HapticFeedback) {
        val currentVibrator = vibratorOrNull() ?: return
        val effect = when (type) {
            HapticFeedback.Click -> VibrationEffect.EFFECT_CLICK
            HapticFeedback.SelectionChanged -> VibrationEffect.EFFECT_TICK
            else -> null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && effect != null) {
            currentVibrator.vibrate(VibrationEffect.createPredefined(effect))
        } else {
            vibrate(type.fallback)
        }
    }

    actual fun vibrateSegments(segments: List<VibrationSegment>, repeatCount: Int) {
        val currentVibrator = vibratorOrNull() ?: return
        val pulses = preparePulses(segments, repeatCount) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && currentVibrator.hasAmplitudeControl()) {
            val timings = LongArray(pulses.size * 2)
            val amplitudes = IntArray(timings.size)
            pulses.forEachIndexed { index, pulse ->
                timings[index * 2] = pulse.delayMillis
                timings[index * 2 + 1] = pulse.durationMillis
                amplitudes[index * 2 + 1] = pulse.strength.toAndroidAmplitude()
            }
            currentVibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        } else {
            currentVibrator.vibrate(pulses.toTimings().toLongArray(), -1)
        }
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
        val normalizedStrength = normalizeStrength(strength) ?: return
        val currentVibrator = vibratorOrNull() ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Amplitude control is only available on API 26+, so older devices fall back to duration only.
            val amplitude = normalizedStrength.toAndroidAmplitude()
            currentVibrator.vibrate(
                VibrationEffect.createOneShot(safeDuration, amplitude)
            )
        } else {
            currentVibrator.vibrate(safeDuration)
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
        val normalizedStrength = normalizeStrength(strength) ?: return
        val currentVibrator = vibratorOrNull() ?: return
        val convertArray = normalizePatternTimings(timings) ?: return
        val repeat = -1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitude = normalizedStrength.toAndroidAmplitude()
            // Android waveforms expect an amplitude per timing slot. Delay slots stay silent with 0 amplitude.
            val amplitudes = IntArray(convertArray.size) { index ->
                if (index % 2 == 0) 0 else amplitude
            }
            currentVibrator.vibrate(VibrationEffect.createWaveform(convertArray, amplitudes, repeat))
        } else {
            currentVibrator.vibrate(convertArray, repeat)
        }
    }

    /**
     * stop Vibrate if running
     */
    actual fun stopVibrate() {
        vibratorOrNull()?.cancel()
    }

}

private fun Float.toAndroidAmplitude(): Int {
    // Android amplitudes are integer steps in the inclusive range 1..255.
    return (this * 255).roundToInt().coerceIn(1, 255)
}
