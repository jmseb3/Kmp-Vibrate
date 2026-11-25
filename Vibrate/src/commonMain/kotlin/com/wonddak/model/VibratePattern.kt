package com.wonddak.model

@Deprecated(
    message = "Deprecated since 1.2.0 and will be removed in a future release. Use VibrationSegment with VibratorManager.vibrateSegments().",
    replaceWith = ReplaceWith(
        "VibrationSegment(duration = vibrate.milliseconds, delay = delay.milliseconds)",
        "com.wonddak.model.VibrationSegment",
        "kotlin.time.Duration.Companion.milliseconds"
    ),
    level = DeprecationLevel.WARNING
)
data class VibratePattern(
    val delay: Long,
    val vibrate: Long
)
