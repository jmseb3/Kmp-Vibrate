import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import dev.wonddak.vibrateExample.App

fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}