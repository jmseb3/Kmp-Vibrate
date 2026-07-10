package com.wonddak

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
        val currentVibrator = vibratorOrNull() ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Amplitude control is only available on API 26+, so older devices fall back to duration only.
            val amplitude = strength.toAndroidAmplitude() ?: return
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
        val currentVibrator = vibratorOrNull() ?: return
        val convertArray = normalizePatternTimings(timings) ?: return
        val repeat = -1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitude = strength.toAndroidAmplitude() ?: return
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

private fun Float.toAndroidAmplitude(): Int? {
    val normalizedStrength = normalizeStrength(this) ?: return null
    // Android amplitudes are integer steps in the inclusive range 1..255.
    return (normalizedStrength * 255).roundToInt().coerceIn(1, 255)
}
