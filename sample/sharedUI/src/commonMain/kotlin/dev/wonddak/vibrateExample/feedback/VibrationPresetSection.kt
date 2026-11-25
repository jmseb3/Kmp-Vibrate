package dev.wonddak.vibrateExample.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.VibrationPreset
import com.wonddak.VibratorManager
import com.wonddak.vibrate
import dev.wonddak.vibrateExample.ui.SampleSection
import dev.wonddak.vibrateExample.ui.asMillisecondsLabel

@Composable
fun VibrationPresetSection(
    enabled: Boolean
) {
    SampleSection(
        title = "Preset patterns",
        subtitle = "Try the built-in patterns and compare how each target behaves."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Reading the enum directly keeps the sample list aligned with the library presets.
            VibrationPreset.entries.forEach { preset ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        VibratorManager.vibrate(preset)
                    },
                    enabled = enabled
                ) {
                    Text("${preset.displayName} (${preset.durationMillis.asMillisecondsLabel()})")
                }
            }
        }
    }
}
