package io.github.takusan23.newradiosupporter.tool.interop

import android.app.PictureInPictureParams
import android.graphics.Rect
import android.os.Build
import android.util.Rational
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PipViewModel : ViewModel() {
    private val _isInPipModeFlow = MutableStateFlow(false)
    val isInPipModeFlow = _isInPipModeFlow.asStateFlow()
    val cardRect = MutableStateFlow(Rect())
    fun onPipModeChanged(isInPictureInPictureMode: Boolean) {
        _isInPipModeFlow.update {
            isInPictureInPictureMode
        }
    }

    fun pipParams(): PictureInPictureParams {
        val rect = cardRect.value
        val builder = PictureInPictureParams.Builder()
        if (Build.VERSION.SDK_INT >= 31) {
            builder.setAutoEnterEnabled(true).setSeamlessResizeEnabled(false)
        }
        builder
            .setSourceRectHint(rect)
            .setAspectRatio(
                Rational(rect.width(), rect.height())
            )
        return builder.build()
    }

}