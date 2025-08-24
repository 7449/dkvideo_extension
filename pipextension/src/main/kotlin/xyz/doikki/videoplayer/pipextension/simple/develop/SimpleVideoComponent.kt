package xyz.doikki.videoplayer.pipextension.simple.develop

import android.content.Context
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper

internal abstract class SimpleVideoComponent(
    context: Context
) : FrameLayout(context, null, 0), SimpleIControlComponent {

    private var _controlWrapper: ControlWrapper? = null
    val controlWrapper get() = _controlWrapper

    override fun attach(controlWrapper: ControlWrapper) {
        _controlWrapper = controlWrapper
    }

    override fun onPlayStateChanged(state: SimpleVideoState) {
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _controlWrapper = null
    }

}