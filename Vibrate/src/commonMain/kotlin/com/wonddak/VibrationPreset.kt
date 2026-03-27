package com.wonddak

import com.wonddak.model.VibratePattern

enum class VibrationPreset(
    val displayName: String,
    val patterns: List<VibratePattern>
) {
    Success(
        displayName = "Success",
        patterns = listOf(
            VibratePattern(delay = 0, vibrate = 40),
            VibratePattern(delay = 45, vibrate = 70)
        )
    ),
    Warning(
        displayName = "Warning",
        patterns = listOf(
            VibratePattern(delay = 0, vibrate = 110),
            VibratePattern(delay = 70, vibrate = 110)
        )
    ),
    Error(
        displayName = "Error",
        patterns = listOf(
            VibratePattern(delay = 0, vibrate = 80),
            VibratePattern(delay = 60, vibrate = 80),
            VibratePattern(delay = 60, vibrate = 80)
        )
    ),
    Heartbeat(
        displayName = "Heartbeat",
        patterns = listOf(
            VibratePattern(delay = 0, vibrate = 90),
            VibratePattern(delay = 120, vibrate = 50)
        )
    ),
    Sos(
        displayName = "SOS",
        patterns = listOf(
            VibratePattern(delay = 0, vibrate = 60),
            VibratePattern(delay = 40, vibrate = 60),
            VibratePattern(delay = 40, vibrate = 60),
            VibratePattern(delay = 120, vibrate = 180),
            VibratePattern(delay = 120, vibrate = 180),
            VibratePattern(delay = 120, vibrate = 180),
            VibratePattern(delay = 120, vibrate = 60),
            VibratePattern(delay = 40, vibrate = 60),
            VibratePattern(delay = 40, vibrate = 60)
        )
    );

    val timings: List<Long>
        get() = flattenPatternTimings(patterns)
}
