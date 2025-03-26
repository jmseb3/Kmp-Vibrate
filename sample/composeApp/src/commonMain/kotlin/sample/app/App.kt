package sample.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.wonddak.vibrateSecond

@Composable
fun App(
    vibratorManager: VibratorManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        singleVibrate(vibratorManager)
        HorizontalDivider()
        TimingVibrate(vibratorManager)
        HorizontalDivider()
        Button(
            onClick = {
                vibratorManager.stopVibrate()
            }
        ) {
            Text("STOP")
        }
    }
}

@Composable
fun singleVibrate(
    vibratorManager: VibratorManager
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var second by remember {
            mutableIntStateOf(3)
        }
        Row(
            modifier = Modifier.padding(5.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            IconButton(
                onClick = {
                    second -= 1
                },
                enabled = second > 1
            ) {
                Icon(Icons.Filled.Remove, null)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, Color.Black, CircleShape)
            ) {
                Text(
                    text = second.toString(),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            IconButton(
                onClick = {
                    second += 1
                }
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
        Button(
            onClick = {
                vibratorManager.vibrateSecond(second)
            }
        ) {
            Text("${second}Sec Vibrate")
        }
    }
}


@Composable
fun TimingVibrate(
    vibratorManager: VibratorManager
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        var timing by remember {
            mutableStateOf(mutableListOf<Long>(
                100,
                500,
                100,
                500
            ))
        }
        val enableButton by remember {
            derivedStateOf {
                timing.isNotEmpty() && timing.size % 2 == 0
            }
        }
        LazyColumn(
            modifier = Modifier.height(200.dp)
                .padding(5.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
        ) {
            itemsIndexed(timing) { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val title = if (index % 2 == 0) {
                            "Delay"
                        } else {
                            "Vibrate"
                        }
                        Text(
                            text = title,
                            modifier = Modifier.width(40.dp)
                        )
                        Text(item.toString())
                    }
                    IconButton(
                        onClick = {}
                    ) {

                    }
                }
            }
        }
        var tempValue by remember {
            mutableStateOf("")
        }
        TextField(
            value = tempValue,
            onValueChange = { text ->
                if (text.isEmpty()) {
                    tempValue = text
                } else {
                    kotlin.runCatching {
                        text.toLong()
                    }.onSuccess {
                        tempValue = text
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    val temp = timing.toMutableList()
                    temp.add(tempValue.toLong())
                    timing = temp
                    tempValue = ""
                }
            )
        )
        Button(
            onClick = {
                vibratorManager.vibratePattern(timing.toLongArray())
            },
            enabled = enableButton
        ) {
            Text("Vibrate Pattern")
        }
    }

}