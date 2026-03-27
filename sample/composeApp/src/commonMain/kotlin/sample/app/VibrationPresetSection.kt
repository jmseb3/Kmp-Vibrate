package sample.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.VibratorManager
import com.wonddak.VibrationPreset
import com.wonddak.vibrate

@Composable
fun VibrationPresetSection(
    enabled: Boolean
) {
    SampleSection(
        title = "Preset patterns",
        subtitle = "Try the built-in patterns and compare how each target behaves."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            VibrationPreset.entries.forEach { preset ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        VibratorManager.vibrate(preset)
                    },
                    enabled = enabled
                ) {
                    Text(preset.displayName)
                }
            }
        }
    }
}
