import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.wonddak.vibrateExample.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() = ComposeViewport { App() }