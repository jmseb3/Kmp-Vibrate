import androidx.compose.ui.window.ComposeUIViewController
import com.wonddak.VibratorManager
import platform.UIKit.UIViewController
import sample.app.App

fun MainViewController(): UIViewController = ComposeUIViewController {
    App(
        vibratorManager = VibratorManager()
    )
}