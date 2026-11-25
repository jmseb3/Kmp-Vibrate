package com.wonddak.internal

import com.wonddak.model.VibrationSegment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds

class VibrationPlaybackTest {
    @Test
    fun replayPreservesInitialDelayAndStrengthOnEveryCycle() {
        val segments = listOf(
            VibrationSegment(60.milliseconds, 0.25f, 10.milliseconds),
            VibrationSegment(90.milliseconds, 1f, 35.milliseconds)
        )
        val cycle = listOf(Pulse(10, 60, 0.25f), Pulse(35, 90, 1f))
        assertEquals(cycle + cycle, preparePulses(segments, repeatCount = 2))
    }

    @Test
    fun rejectsInvalidSegmentsAndUnboundedExpansion() {
        val valid = VibrationSegment(1.milliseconds)
        assertNull(preparePulses(listOf(valid.copy(duration = 999.microseconds)), 1))
        assertNull(preparePulses(listOf(valid.copy(duration = Duration.INFINITE)), 1))
        assertNull(preparePulses(listOf(valid.copy(delay = (-1).microseconds)), 1))
        assertNull(preparePulses(listOf(valid, valid.copy(strength = 0f)), 1))
        assertNull(preparePulses(listOf(valid), 0))
        assertNull(preparePulses(listOf(valid), Int.MAX_VALUE))
        assertNull(preparePulses(List(101) { valid }, 100))
    }
}
