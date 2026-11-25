package com.wonddak.internal

internal const val USE_DURATION = "Use vibrate() with a Kotlin Duration."
internal const val USE_SEGMENTS = "Use vibrateSegments() with VibrationSegment durations. This overload will be removed in a future release."

internal const val MILLISECONDS_IMPORT = "kotlin.time.Duration.Companion.milliseconds"
internal const val VIBRATE_DURATION_IMPORT = "com.wonddak.vibrate"
internal const val VIBRATION_SEGMENT_IMPORT = "com.wonddak.model.VibrationSegment"

// A missing vibration duration becomes zero, so an odd-sized legacy pattern is still rejected.
internal const val RAW_PATTERN_REPLACEMENT =
    "vibrateSegments(timings.chunked(2).map { pair -> " +
        "VibrationSegment(duration = pair.getOrElse(1) { 0L }.milliseconds, delay = pair[0].milliseconds) })"

internal const val RAW_PATTERN_STRENGTH_REPLACEMENT =
    "run { val patternTimings = timings; val segmentStrength = strength; " +
        "vibrateSegments(patternTimings.chunked(2).map { pair -> " +
        "VibrationSegment(duration = pair.getOrElse(1) { 0L }.milliseconds, " +
        "delay = pair[0].milliseconds, strength = segmentStrength) }) }"
