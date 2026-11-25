package com.wonddak

import kotlin.time.Duration

/** A pause followed by a pulse with its own strength in (0, 1]. */
data class VibrationSegment(
    val duration: Duration,
    val strength: Float = 1f,
    val delay: Duration = Duration.ZERO
)
