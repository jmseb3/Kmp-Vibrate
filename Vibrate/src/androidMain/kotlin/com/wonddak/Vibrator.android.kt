package com.wonddak

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Vibrator Manager
 */
@Suppress("DEPRECATION")
actual class VibratorManager(
    context: Context
) {
    private val vibrator: Vibrator

    init {
        println("init ...")
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
    }

    /**
     * make vibrate for [time] second
     *
     * - 3000 = 3Sec
     */
    actual fun vibrate(time: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(time, 100))
        } else {
            vibrator.vibrate(time)
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
        val repeat = -1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(timings, repeat))
        } else {
            vibrator.vibrate(timings, repeat)
        }
    }

    /**
     * stop Vibrate if running
     */
    actual fun stopVibrate() {
        vibrator.cancel()
    }

}