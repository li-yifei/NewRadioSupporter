package io.github.takusan23.newradiosupporter

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import io.github.takusan23.newradiosupporter.tool.interop.PipViewModel
import io.github.takusan23.newradiosupporter.ui.screen.NewRadioSupporterMainScreen

class MainActivity : ComponentActivity() {

    private val pipViewModel: PipViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides this,
            ) {
                NewRadioSupporterMainScreen()
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (!isInPictureInPictureMode) {
            enterPictureInPictureMode(pipViewModel.pipParams())
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        pipViewModel.onPipModeChanged(isInPictureInPictureMode)
    }

}