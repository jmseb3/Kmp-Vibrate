package com.wonddak

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VibratorManagerValidationTest {

    @Test
    fun normalizeDurationRejectsNonPositiveValues() {
        assertNull(normalizeDurationMillis(0))
        assertNull(normalizeDurationMillis(-1))
        assertEquals(250L, normalizeDurationMillis(250))
    }

    @Test
    fun normalizePatternTimingsRejectsEmptyOddAndNegativeInput() {
        assertNull(normalizePatternTimings(emptyList()))
        assertNull(normalizePatternTimings(listOf(100)))
        assertNull(normalizePatternTimings(listOf(0, -100)))
        assertNull(normalizePatternTimings(listOf(0, 0)))
    }

    @Test
    fun normalizePatternTimingsAcceptsDelayVibratePairs() {
        assertContentEquals(
            expected = longArrayOf(0, 80, 40, 120),
            actual = normalizePatternTimings(listOf(0, 80, 40, 120))
        )
    }

    @Test
    fun flattenPatternTimingsConvertsTypedPatterns() {
        val timings = flattenPatternTimings(
            listOf(
                com.wonddak.model.VibratePattern(delay = 10, vibrate = 20),
                com.wonddak.model.VibratePattern(delay = 30, vibrate = 40)
            )
        )

        assertEquals(listOf(10L, 20L, 30L, 40L), timings)
    }
}
