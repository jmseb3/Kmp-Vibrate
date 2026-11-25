package dev.wonddak.vibrateExample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wonddak.VibratorManager
import kotlin.math.roundToInt

@Composable
fun App() {
    // Drive the entire sample from one support check so every section stays in sync per platform.
    val capabilities = VibratorManager.capabilities()
    val supported = capabilities.isSupported
    var strength by remember { mutableFloatStateOf(1f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F5))
            .padding(16.dp)
            .safeContentPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "KMP Vibrate",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "A shared demo for one-shot vibration, custom patterns, and presets.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666)
        )

        SampleSection(
            title = "Device support",
            subtitle = "Available features do not guarantee playback; device settings and browser policies still apply."
        ) {
            SupportBanner(supported = supported)
            Text(
                if (capabilities.supportsAmplitudeControl) {
                    "Strength control: supported"
                } else {
                    "Strength control: unsupported"
                }
            )
            if (capabilities.supportsAmplitudeControl) {
                Text("Strength ${(strength * 100).roundToInt()}%")
                Slider(value = strength, onValueChange = { strength = it })
            }
        }

        HapticFeedbackSection(enabled = supported)
        VibrationPresetSection(enabled = supported)
        singleVibrate(enabled = supported, strength = strength)
        TimingVibrate(enabled = supported, strength = strength)

        Button(
            modifier = Modifier.padding(top = 4.dp),
            onClick = {
                VibratorManager.stopVibrate()
            },
            enabled = supported
        ) {
            Text("STOP")
        }
    }
}
