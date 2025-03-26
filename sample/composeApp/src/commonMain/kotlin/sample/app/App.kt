package sample.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wonddak.VibratorManager

@Composable
fun App(
    vibratorManager: VibratorManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        singleVibrate(vibratorManager)

        Button(
            onClick = {
                vibratorManager.vibratePattern(longArrayOf(500, 1000, 500, 1000), -1)
            }
        ) {
            Text("pattern ")
        }

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
                enabled = second > 0
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
                vibratorManager.vibrate(second.toLong() * 1000)
            }
        ) {
            Text("Vibrate")
        }
    }
}