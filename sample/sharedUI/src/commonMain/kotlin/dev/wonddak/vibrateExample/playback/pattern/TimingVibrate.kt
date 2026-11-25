package dev.wonddak.vibrateExample.playback.pattern

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.wonddak.VibratorManager
import com.wonddak.model.VibrationSegment
import dev.wonddak.vibrateExample.ui.SampleSection
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TimingVibrate(enabled: Boolean, strength: Float) {
    var pattern by remember {
        mutableStateOf(List(2) { VibrationSegment(500.milliseconds, delay = 100.milliseconds) })
    }
    var repeats by remember { mutableStateOf("1") }
    val repeatCount = repeats.toIntOrNull()?.takeIf { it in 1..100 }

    SampleSection(
        title = "Custom pattern",
        subtitle = "Pattern items are entered in milliseconds: delay first, then vibrate."
    ) {
        PatternItems(pattern) { index ->
            pattern = pattern.toMutableList().apply { removeAt(index) }
        }
        Text("Use milliseconds for finer control.", color = Color(0xFF666666))
        PatternItemEditor(canAddItem = pattern.size < 100) { item -> pattern = pattern + item }
        OutlinedTextField(
            value = repeats,
            onValueChange = { repeats = it },
            label = { Text("Repeat count (1-100)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = repeats.isNotEmpty() && repeatCount == null
        )
        PatternTimeline(pattern)
        if (pattern.isNotEmpty() && repeatCount != null) {
            PatternCodeExport(pattern, repeatCount, strength)
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                VibratorManager.vibrateSegments(
                    pattern.map { it.copy(strength = strength) }, checkNotNull(repeatCount)
                )
            },
            enabled = pattern.isNotEmpty() && repeatCount != null && enabled
        ) {
            Text("Vibrate Pattern")
        }
    }
}
