package xyz.doikki.videoplayer.pipextension.view.pip

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPipErrorBinding
import xyz.doikki.videoplayer.pipextension.listener.OnPipErrorListener
import xyz.doikki.videoplayer.pipextension.view.gone
import xyz.doikki.videoplayer.pipextension.view.visible
import xyz.doikki.videoplayer.player.VideoView

internal class PipErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), IControlComponent {

    private var wrapper: ControlWrapper? = null
    private var listener: OnPipErrorListener? = null
    private val viewBinding = VideoLayoutPlayPipErrorBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        gone()
        viewBinding.close.setOnClickListener { listener?.onPipErrorClickClose() }
        viewBinding.restore.setOnClickListener { listener?.onPipErrorClickRestore() }
        viewBinding.next.setOnClickListener { listener?.onPipErrorClickNext() }
        viewBinding.prev.setOnClickListener { listener?.onPipErrorClickPrev() }
        viewBinding.retry.setOnClickListener { wrapper?.replay(false) }
    }

    fun registerListener(listener: OnPipErrorListener?) = apply {
        this.listener = listener
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

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        val isPlayList = listener?.onPipErrorPlayList() ?: return
        if (!isPlayList) {
            viewBinding.next.gone()
            viewBinding.prev.gone()
        }
    }

    override fun setProgress(duration: Int, position: Int) {}
    override fun onPlayerStateChanged(playerState: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {}
    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {}

}