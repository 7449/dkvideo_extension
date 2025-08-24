package xyz.doikki.videoplayer.pipextension.simple.develop

import android.view.View
import android.view.animation.Animation
import xyz.doikki.videoplayer.controller.IControlComponent

internal interface SimpleIControlComponent : IControlComponent {

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