package sample.app

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.wonddak.vibrateSecond

@Composable
fun singleVibrate(enabled: Boolean) {
    SampleSection(
        title = "Single vibrate",
        subtitle = "Pick a duration in seconds and trigger a one-shot vibration."
    ) {
        var second by remember {
            mutableIntStateOf(3)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(14.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
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
                        .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(12.dp))
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
                    VibratorManager.vibrateSecond(second)
                },
                enabled = enabled
            ) {
                Text("${second}s Vibrate")
            }
        }
    }
}
