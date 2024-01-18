package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.view.View
import android.view.animation.Animation
import xyz.doikki.videoplayer.controller.IControlComponent

interface SimpleIControlComponent : IControlComponent {

    override fun getView(): View? {
        return this as? View
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
    }

    override fun setProgress(duration: Int, position: Int) {
    }

    override fun onPlayStateChanged(playState: Int) {
    }

    override fun onPlayerStateChanged(playerState: Int) {
    }

    override fun onLockStateChanged(isLocked: Boolean) {
    }

}