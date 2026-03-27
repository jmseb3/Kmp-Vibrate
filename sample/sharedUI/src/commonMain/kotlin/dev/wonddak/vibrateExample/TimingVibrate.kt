package dev.wonddak.vibrateExample

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wonddak.VibratorManager
import com.wonddak.model.VibratePattern
import com.wonddak.vibratePattern

@Composable
fun TimingVibrate(enabled: Boolean) {
    SampleSection(
        title = "Custom pattern",
        subtitle = "Pattern items are entered in milliseconds: delay first, then vibrate."
    ) {
        // Keep user edits as typed delay/vibrate pairs; the library flattens them right before play.
        var timing by remember {
            mutableStateOf(
                mutableListOf<VibratePattern>(
                    VibratePattern(100, 500),
                    VibratePattern(100, 500)
                )
            )
        }
        val enableVibrateButton by remember {
            derivedStateOf {
                timing.isNotEmpty()
            }
        }
        LazyColumn(
            modifier = Modifier
                .height(220.dp)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(14.dp))
                .padding(vertical = 4.dp)
        ) {
            itemsIndexed(
                items = timing
            ) { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf(
                            "Delay ${item.delay.asMillisecondsLabel()}",
                            "Vibrate ${item.vibrate.asMillisecondsLabel()}"
                        ).forEach { title ->
                            Text(
                                title
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            // Replace the list instance so Compose observes the change.
                            val temp = timing.toMutableList()
                            temp.removeAt(index)
                            timing = temp
                        }
                    ) {
                        Icon(Icons.Filled.Clear, null)
                    }
                }
            }
        }
        Text(
            text = "Use milliseconds for finer control.",
            color = Color(0xFF666666)
        )
        var delay by remember {
            mutableStateOf("")
        }
        var vibrate by remember {
            mutableStateOf("")
        }
        val enableAddButton by remember(delay, vibrate) {
            derivedStateOf {
                delay.isNotEmpty() && vibrate.isNotEmpty()
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Add pattern item")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = delay,
                    onValueChange = { text ->
                        if (text.isEmpty()) {
                            delay = text
                        } else {
                            // Reject non-numeric text early so the add action always produces valid timings.
                            runCatching {
                                text.toLong()
                            }.onSuccess {
                                delay = text
                            }
                        }
                    },
                    placeholder = {
                        Text("Delay (ms)")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = vibrate,
                    onValueChange = { text ->
                        if (text.isEmpty()) {
                            vibrate = text
                        } else {
                            // Reject non-numeric text early so the add action always produces valid timings.
                            runCatching {
                                text.toLong()
                            }.onSuccess {
                                vibrate = text
                            }
                        }
                    },
                    placeholder = {
                        Text("Vibrate (ms)")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
            Button(
                onClick = {
                    // Replace the list instance so Compose observes the appended pattern item.
                    val temp = timing.toMutableList()
                    temp.add(
                        VibratePattern(delay.toLong(), vibrate.toLong())
                    )
                    timing = temp
                },
                enabled = enableAddButton
            ) {
                Text("Add Vibrate Pattern")
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                VibratorManager.vibratePattern(timing)
            },
            enabled = enableVibrateButton && enabled
        ) {
            Text("Vibrate Pattern")
        }
    }
}
