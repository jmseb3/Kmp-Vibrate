package com.wonddak.internal

import com.wonddak.model.VibratePattern

internal fun normalizeDurationMillis(durationMillis: Long): Long? {
    return durationMillis.takeIf { it > 0L }
}

internal fun normalizeStrength(strength: Float): Float? {
    if (strength.isNaN()) {
        return null
    }

    // Treat 0f as a no-op so callers get the same "do nothing" behavior on every target.
    val normalizedStrength = strength.coerceIn(0f, 1f)
    return normalizedStrength.takeIf { it > 0f }
}

internal fun flattenPatternTimings(patterns: List<VibratePattern>): List<Long> {
    // Platform implementations consume a flat [delay, vibrate, delay, vibrate] sequence.
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
        // Odd indexes are the actual vibration windows, so they must stay positive.
        if (index % 2 == 1 && time == 0L) {
            return null
        }
    }

    return timings.toLongArray()
}
