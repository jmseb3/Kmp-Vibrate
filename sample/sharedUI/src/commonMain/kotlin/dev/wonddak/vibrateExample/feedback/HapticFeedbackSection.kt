package dev.wonddak.vibrateExample.feedback

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.wonddak.HapticFeedback
import com.wonddak.VibrationPreset
import com.wonddak.model.VibrationSegment
import com.wonddak.VibratorManager
import com.wonddak.vibrate
import dev.wonddak.vibrateExample.ui.SampleSection
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun HapticFeedbackSection(enabled: Boolean) {
    SampleSection(
        title = "UI feedback",
        subtitle = "Uses system effects where available and timing presets on other targets."
    ) {
        for (type in HapticFeedback.entries) {
            Button(onClick = { VibratorManager.feedback(type) }, enabled = enabled) {
                Text(if (type == HapticFeedback.SelectionChanged) "Selection changed" else type.name)
            }
        }
        Button(onClick = { VibratorManager.vibrate(VibrationPreset.Click) }, enabled = enabled) {
            Text("Compare with Click preset")
        }
        Button(
            onClick = {
                VibratorManager.vibrateSegments(listOf(
                    VibrationSegment(60.milliseconds, strength = 0.3f),
                    VibrationSegment(90.milliseconds, strength = 1f, delay = 80.milliseconds)
                ))
            },
            enabled = enabled
        ) {
            Text("Try a soft then strong pulse")
        }
    }
}
