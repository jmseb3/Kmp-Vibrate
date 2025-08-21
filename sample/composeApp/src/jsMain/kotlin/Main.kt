
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.skiko.wasm.onWasmReady
import sample.app.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    window.onload = {
        onWasmReady {
            val body = document.body ?: return@onWasmReady
            ComposeViewport(body) {
                App()
            }
        }
    }
}