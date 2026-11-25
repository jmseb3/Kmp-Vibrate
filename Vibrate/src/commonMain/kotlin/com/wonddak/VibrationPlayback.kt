package com.wonddak

import com.wonddak.internal.normalizePatternTimings
import com.wonddak.model.VibratePattern
import kotlin.jvm.JvmName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/** Plays for a finite duration, truncated to whole milliseconds. */
fun VibratorManager.vibrate(duration: Duration) {
    vibrate(duration, strength = 1f)
}

/** Durations shorter than one millisecond, negative, or infinite are ignored. */
fun VibratorManager.vibrate(duration: Duration, strength: Float) {
    if (duration.isFinite() && duration >= 1.milliseconds) {
        vibrate(duration.inWholeMilliseconds, strength)
    }
}

/** Plays a preset [repeatCount] times in total. */
fun VibratorManager.vibrate(preset: VibrationPreset, repeatCount: Int, strength: Float = 1f) {
    vibratePattern(preset.patterns, repeatCount, strength)
}

/** Plays a typed pattern [repeatCount] times in total. */
fun VibratorManager.vibratePattern(
    timings: List<VibratePattern>,
    repeatCount: Int,
    strength: Float = 1f
) {
    vibrateSegments(
        timings.map { VibrationSegment(it.vibrate.milliseconds, strength, it.delay.milliseconds) },
        repeatCount
    )
}

/** Plays flat delay/vibration pairs [repeatCount] times in total. */
@JvmName("vibrateRawPatternRepeated")
fun VibratorManager.vibratePattern(
    timings: List<Long>,
    repeatCount: Int,
    strength: Float = 1f
) {
    val validated = normalizePatternTimings(timings) ?: return
    vibrateSegments(
        List(validated.size / 2) { index ->
            VibrationSegment(validated[index * 2 + 1].milliseconds, strength, validated[index * 2].milliseconds)
        },
        repeatCount
    )
}
