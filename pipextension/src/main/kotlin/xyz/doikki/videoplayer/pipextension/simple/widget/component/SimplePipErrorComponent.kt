package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPipErrorBinding
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.visible
import xyz.doikki.videoplayer.player.VideoView

class PipErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), IControlComponent {

    private var wrapper: ControlWrapper? = null
    private val viewBinding = VideoLayoutPlayPipErrorBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        gone()
        viewBinding.close.setOnClickListener {}
        viewBinding.restore.setOnClickListener { }
        viewBinding.next.setOnClickListener { }
        viewBinding.prev.setOnClickListener { }
        viewBinding.retry.setOnClickListener { }
    }

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onPlayStateChanged(playState: Int) {
        if (playState == VideoView.STATE_ERROR) {
            bringToFront()
            visible()
        } else {
            gone()
        }
    }

    override fun setProgress(duration: Int, position: Int) {}
    override fun onPlayerStateChanged(playerState: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {}
    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {}

}