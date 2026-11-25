package dev.wonddak.vibrateExample.playback.pattern

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wonddak.model.VibrationSegment
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun PatternItemEditor(canAddItem: Boolean, onAdd: (VibrationSegment) -> Unit) {
    var delay by remember { mutableStateOf("") }
    var vibrate by remember { mutableStateOf("") }
    val delayMillis = delay.toLongOrNull()?.takeIf { it in 0L..Int.MAX_VALUE.toLong() }
    val vibrateMillis = vibrate.toLongOrNull()?.takeIf { it in 1L..Int.MAX_VALUE.toLong() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Add pattern item")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = delay,
                onValueChange = { if (it.isEmpty() || it.toLongOrNull() != null) delay = it },
                placeholder = { Text("Delay (ms)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = vibrate,
                onValueChange = { if (it.isEmpty() || it.toLongOrNull() != null) vibrate = it },
                placeholder = { Text("Vibrate (ms)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }
        Button(
            onClick = {
                onAdd(
                    VibrationSegment(
                        duration = checkNotNull(vibrateMillis).milliseconds,
                        delay = checkNotNull(delayMillis).milliseconds
                    )
                )
            },
            enabled = canAddItem && delayMillis != null && vibrateMillis != null
        ) {
            Text("Add Vibrate Pattern")
        }
    }
}
