package dev.wonddak.vibrateExample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SampleSection(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE4E4E4)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                }
            }
            content()
        }
    }
}

@Composable
fun SupportBanner(supported: Boolean) {
    // This mirrors VibratorManager.isSupported(), so it reflects target capability rather than user settings.
    val backgroundColor = if (supported) Color(0xFFEAF7EE) else Color(0xFFFDECEC)
    val accentColor = if (supported) Color(0xFF1E8E3E) else Color(0xFFC62828)
    val statusText = if (supported) "Supported" else "Unsupported"
    val detailText = if (supported) {
        "This target can vibrate and play haptic patterns."
    } else {
        "This target does not expose vibration support."
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(12.dp),
                shape = RoundedCornerShape(999.dp),
                color = accentColor
            ) {}
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelLarge,
                    color = accentColor
                )
                Text(
                    text = detailText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

fun Long.asMillisecondsLabel(): String = "$this ms"
