@file:Suppress("DEPRECATION") // Keep the legacy patterns property available.

package com.wonddak

import com.wonddak.model.VibratePattern
import kotlin.time.Duration.Companion.milliseconds

enum class VibrationPreset(
    val displayName: String,
    val segments: List<VibrationSegment>
) {
    Click(
        displayName = "Click",
        segments = listOf(
            VibrationSegment(25.milliseconds)
        )
    ),
    DoubleClick(
        displayName = "Double click",
        segments = listOf(
            VibrationSegment(25.milliseconds),
            VibrationSegment(25.milliseconds, delay = 45.milliseconds)
        )
    ),
    Tick(
        displayName = "Tick",
        segments = listOf(
            VibrationSegment(12.milliseconds)
        )
    ),
    Success(
        displayName = "Success",
        segments = listOf(
            VibrationSegment(40.milliseconds),
            VibrationSegment(70.milliseconds, delay = 45.milliseconds)
        )
    ),
    Warning(
        displayName = "Warning",
        segments = listOf(
            VibrationSegment(110.milliseconds),
            VibrationSegment(110.milliseconds, delay = 70.milliseconds)
        )
    ),
    Error(
        displayName = "Error",
        segments = listOf(
            VibrationSegment(80.milliseconds),
            VibrationSegment(80.milliseconds, delay = 60.milliseconds),
            VibrationSegment(80.milliseconds, delay = 60.milliseconds)
        )
    ),
    Heartbeat(
        displayName = "Heartbeat",
        segments = listOf(
            VibrationSegment(90.milliseconds),
            VibrationSegment(50.milliseconds, delay = 120.milliseconds)
        )
    ),
    Sos(
        displayName = "SOS",
        segments = listOf(
            VibrationSegment(60.milliseconds),
            VibrationSegment(60.milliseconds, delay = 40.milliseconds),
            VibrationSegment(60.milliseconds, delay = 40.milliseconds),
            VibrationSegment(180.milliseconds, delay = 120.milliseconds),
            VibrationSegment(180.milliseconds, delay = 120.milliseconds),
            VibrationSegment(180.milliseconds, delay = 120.milliseconds),
            VibrationSegment(60.milliseconds, delay = 120.milliseconds),
            VibrationSegment(60.milliseconds, delay = 40.milliseconds),
            VibrationSegment(60.milliseconds, delay = 40.milliseconds)
        )
    );

    @Deprecated(
        message = "Use segments to inspect or customize this preset. This property will be removed in a future release.",
        replaceWith = ReplaceWith("segments")
    )
    val patterns: List<VibratePattern> = segments.map {
        VibratePattern(delay = it.delay.inWholeMilliseconds, vibrate = it.duration.inWholeMilliseconds)
    }

    val timings: List<Long>
        get() = segments.flatMap { listOf(it.delay.inWholeMilliseconds, it.duration.inWholeMilliseconds) }

    val durationMillis: Long
        get() = timings.sum()
}
