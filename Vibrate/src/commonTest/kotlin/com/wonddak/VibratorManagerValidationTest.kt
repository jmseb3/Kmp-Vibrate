package com.wonddak

import com.wonddak.model.VibratePattern
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
    fun vibrationPresetExposesTimingsAndDuration() {
        assertEquals(listOf(0L, 25L, 45L, 25L), VibrationPreset.DoubleClick.timings)
        assertEquals(95L, VibrationPreset.DoubleClick.durationMillis)
    }

    @Test
    fun normalizeStrengthClampsAndRejectsInvalidValues() {
        // Shared validation keeps Android/iOS mappings safe while Web simply ignores strength.
        assertNull(normalizeStrength(Float.NaN))
        assertNull(normalizeStrength(0f))
        assertEquals(0.25f, normalizeStrength(0.25f))
        assertEquals(1f, normalizeStrength(3f))
    }

    @Test
    fun flattenPatternTimingsConvertsTypedPatterns() {
        val timings = flattenPatternTimings(
            listOf(
                VibratePattern(delay = 10, vibrate = 20),
                VibratePattern(delay = 30, vibrate = 40)
            )
        )

        assertEquals(listOf(10L, 20L, 30L, 40L), timings)
    }
}
