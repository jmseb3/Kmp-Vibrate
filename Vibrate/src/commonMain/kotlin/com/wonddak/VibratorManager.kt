package com.wonddak

import com.wonddak.model.VibratePattern

/**
 * Vibrator Manager
 */
expect object VibratorManager {

    /**
     * make vibrate for [time] second
     *
     * - 3000 = 3 Second
     */
    fun vibrate(time: Long)

    /**
     * make vibrate for [timings]
     * - 3000 = 3 Second
     * @param[timings] off/on Timing
     *
     * - if (300,500,700,500) > 0.3 delay > 0.5 vibrate > 0.7 delay . 0.5 vibrate
     */
    fun vibratePattern(timings: List<Long>)

    /**
     * stop Vibrate if running
     */
    fun stopVibrate()
}

/**
 * extension func for [VibratorManager.vibrate]
 * @param[second] vibrate second, if 1 to change to 1000(Long) and run [VibratorManager.vibrate]
 */
fun VibratorManager.vibrateSecond(second: Int) = vibrate(
    second.toLong() * 1000
)

/**
 * extension func for [VibratorManager.vibratePattern]
 * @param[timings] [VibratePattern] to [LongArray] and run [VibratorManager.vibratePattern]
 */
fun VibratorManager.vibratePattern(timings: List<VibratePattern>) = vibratePattern(
    timings.flatMap { listOf(it.delay, it.vibrate) }
)