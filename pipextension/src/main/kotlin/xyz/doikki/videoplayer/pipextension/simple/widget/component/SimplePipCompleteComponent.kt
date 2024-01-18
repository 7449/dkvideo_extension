package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPipCompleteBinding
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleIControlComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoState
import xyz.doikki.videoplayer.pipextension.visible

class SimplePipCompleteComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), SimpleIControlComponent {

    private var wrapper: ControlWrapper? = null
    private val viewBinding = VideoLayoutPlayPipCompleteBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        gone()
        viewBinding.close.setOnClickListener { VideoManager.closePipMode() }
        viewBinding.restore.setOnClickListener { VideoManager.pipComeBackActivity() }
        viewBinding.next.setOnClickListener { VideoManager.videoPlayNext() }
        viewBinding.prev.setOnClickListener { VideoManager.videoPlayPrev() }
        viewBinding.replay.setOnClickListener { wrapper?.replay(true) }
    }

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun onPlayStateChanged(state: SimpleVideoState) {
        if (state == SimpleVideoState.PLAYBACK_COMPLETED) {
            bringToFront()
            visible()
            if (VideoManager.isPlayList()) {
                viewBinding.next.visible()
                viewBinding.prev.visible()
            } else {
                viewBinding.next.gone()
                viewBinding.prev.gone()
            }
        } else {
            gone()
        }
    }

}