package dev.wonddak.vibrateExample.playback.pattern

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.wonddak.model.VibratePattern

@Suppress("DEPRECATION")
@Composable
internal fun PatternCodeExport(timing: List<VibratePattern>, repeatCount: Int, strength: Float) {
    val patternCode = timing.joinToString(",\n") { "    VibratePattern(${it.delay}, ${it.vibrate})" }
    val code = "VibratorManager.vibratePattern(listOf(\n$patternCode\n), " +
        "repeatCount = $repeatCount, strength = ${strength}f)"
    val clipboard = LocalClipboardManager.current
    SelectionContainer { Text(code) }
    Button(onClick = { clipboard.setText(AnnotatedString(code)) }) { Text("Copy Kotlin code") }
}
