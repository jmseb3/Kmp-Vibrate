package com.wonddak

/** The UI event to acknowledge with haptic feedback. */
enum class HapticFeedback(internal val fallback: VibrationPreset) {
    SelectionChanged(VibrationPreset.Tick),
    Click(VibrationPreset.Click),
    Success(VibrationPreset.Success),
    Warning(VibrationPreset.Warning),
    Error(VibrationPreset.Error)
}
