package com.wonddak

import com.wonddak.model.VibratePattern

/**
 * Vibrator Manager
 */
expect object VibratorManager {

    /**
     * returns true when the current platform can execute vibration or haptic feedback.
     */
    fun isSupported(): Boolean

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
    flattenPatternTimings(timings)
)

/**
 * extension func for common presets
 */
fun VibratorManager.vibrate(preset: VibrationPreset) = vibratePattern(
    preset.patterns
)

internal fun normalizeDurationMillis(durationMillis: Long): Long? {
    return durationMillis.takeIf { it > 0L }
}

internal fun flattenPatternTimings(patterns: List<VibratePattern>): List<Long> {
    return patterns.flatMap { listOf(it.delay, it.vibrate) }
}

internal fun normalizePatternTimings(timings: List<Long>): LongArray? {
    if (timings.isEmpty() || timings.size % 2 != 0) {
        return null
    }

    timings.forEachIndexed { index, time ->
        if (time < 0L) {
            return null
        }
        if (index % 2 == 1 && time == 0L) {
            return null
        }
    }

    return timings.toLongArray()
}
