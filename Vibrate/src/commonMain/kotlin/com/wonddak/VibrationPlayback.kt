package com.wonddak

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

internal data class Pulse(val delayMillis: Long, val durationMillis: Long, val strength: Float)

internal fun preparePulses(segments: List<VibrationSegment>, repeatCount: Int): List<Pulse>? {
    if (repeatCount !in 1..100 || segments.isEmpty() || segments.size > 10_000 / repeatCount) {
        return null
    }
    var iterationMillis = 0L
    val limit = Long.MAX_VALUE / repeatCount
    val pulses = segments.map { segment ->
        if (!segment.delay.isFinite() || segment.delay < Duration.ZERO ||
            !segment.duration.isFinite() || segment.duration < 1.milliseconds
        ) return null
        val delay = segment.delay.inWholeMilliseconds
        val duration = segment.duration.inWholeMilliseconds
        val strength = normalizeStrength(segment.strength) ?: return null
        if (delay > limit - iterationMillis || duration > limit - iterationMillis - delay) return null
        iterationMillis += delay + duration
        Pulse(delay, duration, strength)
    }
    return List(pulses.size * repeatCount) { pulses[it % pulses.size] }
}

internal fun List<Pulse>.toTimings(): List<Long> = flatMap { listOf(it.delayMillis, it.durationMillis) }
