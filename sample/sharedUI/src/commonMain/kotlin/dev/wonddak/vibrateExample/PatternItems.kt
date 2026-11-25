package dev.wonddak.vibrateExample

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wonddak.model.VibratePattern

@Composable
internal fun PatternItems(pattern: List<VibratePattern>, onRemove: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .height(220.dp)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(14.dp))
            .padding(vertical = 4.dp)
    ) {
        itemsIndexed(pattern) { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Delay ${item.delay.asMillisecondsLabel()}")
                    Text("Vibrate ${item.vibrate.asMillisecondsLabel()}")
                }
                IconButton(onClick = { onRemove(index) }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Remove pattern item")
                }
            }
        }
    }
}
