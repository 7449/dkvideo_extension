package xyz.doikki.videoplayer.pipextension.view.pip

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPipCompleteBinding
import xyz.doikki.videoplayer.pipextension.listener.OnPipCompleteListener
import xyz.doikki.videoplayer.pipextension.view.gone
import xyz.doikki.videoplayer.pipextension.view.visible
import xyz.doikki.videoplayer.player.VideoView

internal class VideoPipCompleteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IControlComponent {

    private var wrapper: ControlWrapper? = null
    private var listener: OnPipCompleteListener? = null
    private val viewBinding = VideoLayoutPlayPipCompleteBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        gone()
        viewBinding.close.setOnClickListener { listener?.onPipCompleteClickClose() }
        viewBinding.restore.setOnClickListener { listener?.onPipCompleteClickRestore() }
        viewBinding.next.setOnClickListener { listener?.onPipCompleteClickNext() }
        viewBinding.prev.setOnClickListener { listener?.onPipCompleteClickPrev() }
        viewBinding.replay.setOnClickListener { wrapper?.replay(true) }
    }

    fun registerListener(listener: OnPipCompleteListener?) = apply {
        this.listener = listener
    }

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onPlayStateChanged(playState: Int) {
        if (playState == VideoView.STATE_PLAYBACK_COMPLETED) {
            bringToFront()
            visible()
        } else {
            gone()
        }
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {}
    override fun setProgress(duration: Int, position: Int) {}
    override fun onPlayerStateChanged(playerState: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {}

}