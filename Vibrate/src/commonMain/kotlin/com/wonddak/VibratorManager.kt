@file:Suppress("DEPRECATION") // Retain the legacy typed overloads.

package com.wonddak

import com.wonddak.internal.flattenPatternTimings
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
     * Returns current vibration support and strength-control capabilities without playing feedback.
     * This does not guarantee playback; user settings and platform policies still apply.
     */
    fun capabilities(): VibrationCapabilities

    /** Plays feedback for a UI event, using a timing preset where needed. */
    fun feedback(type: HapticFeedback)

    /**
     * Plays segments [repeatCount] times in total. Each iteration includes its initial delay.
     * Platforms without amplitude control play the same timings at their default strength.
     * Ignores invalid segments, repeat counts outside 1..100, and patterns expanding
     * to more than 10,000 pulses. Platform playback limits still apply.
     */
    fun vibrateSegments(segments: List<VibrationSegment>, repeatCount: Int = 1)

    /**
     * make vibrate for [time] second
     *
     * - 3000 = 3 Second
     */
    fun vibrate(time: Long)

    /**
     * make vibrate for [time] milliseconds with [strength].
     * [strength] is normalized between 0f and 1f.
     */
    fun vibrate(time: Long, strength: Float)

    /**
     * make vibrate for [timings]
     * - 3000 = 3 Second
     * @param[timings] off/on Timing
     *
     * - if (300,500,700,500) > 0.3 delay > 0.5 vibrate > 0.7 delay . 0.5 vibrate
     */
    fun vibratePattern(timings: List<Long>)

    /**
     * make vibrate for [timings] with [strength].
     * [strength] is normalized between 0f and 1f.
     */
    fun vibratePattern(timings: List<Long>, strength: Float)

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
 * extension func for [VibratorManager.vibrate] with strength.
 */
fun VibratorManager.vibrateSecond(second: Int, strength: Float) = vibrate(
    second.toLong() * 1000,
    strength
)

/**
 * extension func for [VibratorManager.vibratePattern]
 * @param[timings] [VibratePattern] to [LongArray] and run [VibratorManager.vibratePattern]
 */
@Deprecated(
    message = "Use vibrateSegments() with a list of VibrationSegment. This overload will be removed in a future release.",
    replaceWith = ReplaceWith(
        "vibrateSegments(timings.map { VibrationSegment(duration = it.vibrate.milliseconds, delay = it.delay.milliseconds) })",
        "com.wonddak.VibrationSegment",
        "kotlin.time.Duration.Companion.milliseconds"
    )
)
fun VibratorManager.vibratePattern(timings: List<VibratePattern>) = vibratePattern(
    flattenPatternTimings(timings)
)

/**
 * extension func for [VibratorManager.vibratePattern] with strength.
 */
@Deprecated(
    message = "Use vibrateSegments() with a list of VibrationSegment. This overload will be removed in a future release."
)
fun VibratorManager.vibratePattern(timings: List<VibratePattern>, strength: Float) = vibratePattern(
    flattenPatternTimings(timings),
    strength
)

/**
 * extension func for common presets
 */
fun VibratorManager.vibrate(preset: VibrationPreset) = vibratePattern(
    preset.timings
)

/**
 * extension func for common presets with strength.
 */
fun VibratorManager.vibrate(preset: VibrationPreset, strength: Float) = vibratePattern(
    preset.timings,
    strength
)
