package dev.wonddak.vibrateExample

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.wonddak.model.VibratePattern

@Composable
internal fun PatternTimeline(pattern: List<VibratePattern>) {
    if (pattern.isEmpty()) return
    val totalMillis = pattern.sumOf { it.delay + it.vibrate }
    val pulseColor = MaterialTheme.colorScheme.primary
    val pauseColor = MaterialTheme.colorScheme.surfaceVariant
    Text("One cycle: $totalMillis ms. Highlighted blocks are vibration.")
    Canvas(Modifier.fillMaxWidth().height(16.dp)) {
        drawRect(pauseColor)
        var position = 0L
        val pixelsPerMillis = size.width / totalMillis.toDouble()
        for (pulse in pattern) {
            position += pulse.delay
            drawRect(
                color = pulseColor,
                topLeft = Offset((position * pixelsPerMillis).toFloat(), 0f),
                size = Size((pulse.vibrate * pixelsPerMillis).toFloat(), size.height)
            )
            position += pulse.vibrate
        }
    }
}
