import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.wonddak.vibrateExample.App

@OptIn(ExperimentalComposeUiApi::class)
// Mount the shared sample UI into the browser entry point.
fun main() = ComposeViewport { App() }
