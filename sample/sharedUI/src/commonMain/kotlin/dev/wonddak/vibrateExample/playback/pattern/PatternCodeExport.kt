package dev.wonddak.vibrateExample.playback.pattern

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.wonddak.VibrationSegment

@Suppress("DEPRECATION")
@Composable
internal fun PatternCodeExport(timing: List<VibrationSegment>, repeatCount: Int, strength: Float) {
    val patternCode = timing.joinToString(",\n") {
        "    VibrationSegment(${it.duration.inWholeMilliseconds}.milliseconds, " +
            "delay = ${it.delay.inWholeMilliseconds}.milliseconds, strength = ${strength}f)"
    }
    val code = "import com.wonddak.VibrationSegment\n" +
        "import com.wonddak.VibratorManager\n" +
        "import kotlin.time.Duration.Companion.milliseconds\n\n" +
        "VibratorManager.vibrateSegments(listOf(\n$patternCode\n), repeatCount = $repeatCount)"
    val clipboard = LocalClipboardManager.current
    SelectionContainer { Text(code) }
    Button(onClick = { clipboard.setText(AnnotatedString(code)) }) { Text("Copy Kotlin code") }
}
