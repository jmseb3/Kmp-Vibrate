@file:Suppress("DEPRECATION") // Keep coverage for the legacy pattern API.

package com.wonddak

import com.wonddak.internal.flattenPatternTimings
import com.wonddak.internal.normalizeDurationMillis
import com.wonddak.internal.normalizePatternTimings
import com.wonddak.internal.normalizeStrength
import com.wonddak.model.VibratePattern
import com.wonddak.model.VibrationSegment
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds

class VibratorManagerValidationTest {

    @Test
    fun capabilitiesMatchSupportAndDoNotAdvertiseStrengthOnUnsupportedTargets() {
        val capabilities = VibratorManager.capabilities()

        assertEquals(VibratorManager.isSupported(), capabilities.isSupported)
        if (!capabilities.isSupported) {
            assertFalse(capabilities.supportsAmplitudeControl)
        }
    }

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
        assertEquals(
            listOf(VibrationSegment(25.milliseconds), VibrationSegment(25.milliseconds, delay = 45.milliseconds)),
            VibrationPreset.DoubleClick.segments
        )
        assertEquals(
            listOf(VibratePattern(0, 25), VibratePattern(45, 25)),
            VibrationPreset.DoubleClick.patterns
        )
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
