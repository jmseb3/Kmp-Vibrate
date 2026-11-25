package com.wonddak

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

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
