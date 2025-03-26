package com.wonddak

/**
 * Vibrator Manager
 */
expect class VibratorManager {

    /**
     * make vibrate for [time] second
     *
     * - 3000 = 3Sec
     */
    fun vibrate(time: Long)

    /**
     * make vibrate for [timings]
     * - 3000 = 3Sec
     * @param[timings] off/on Timing
     *
     * - if \[300,500,700,500] > 0.3 delay > 0.5 vibrate > 0.7 delay . 0.5 vibrate
     */
    fun vibratePattern(timings: LongArray)

    /**
     * stop Vibrate if running
     */
    fun stopVibrate()
}

/**
 * extension func for VibratorManager
 * @param[second] vibrate second, if 1 to change to 1000(Long) and run [VibratorManager.vibrate]
 */
fun VibratorManager.vibrateSecond(second: Int) = vibrate(second.toLong() * 1000)