package xyz.doikki.videoplayer.pipextension.simple.develop

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoComponent.SimpleIControlComponent

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

    private interface SimpleIControlComponent : IControlComponent {

        override fun getView(): View? {
            return this as? View
        }

        override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
        }

        override fun setProgress(duration: Int, position: Int) {
        }

        override fun onPlayStateChanged(playState: Int) {
            val videoState = SimpleVideoState.entries.find { it.state == playState }
            requireNotNull(videoState) { "Unknown Video State $playState" }
            onPlayStateChanged(videoState)
        }

        override fun onPlayerStateChanged(playerState: Int) {
        }

        override fun onLockStateChanged(isLocked: Boolean) {
        }

        fun onPlayStateChanged(state: SimpleVideoState)

    }

}