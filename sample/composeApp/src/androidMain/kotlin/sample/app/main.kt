package sample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wonddak.VibratorContextProvider
import com.wonddak.VibratorManager

class AppActivity : ComponentActivity() {
    private val vibratorManager by lazy {
        VibratorManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        VibratorContextProvider.init(this@AppActivity)
        setContent {
            App(vibratorManager)
        }
    }
}