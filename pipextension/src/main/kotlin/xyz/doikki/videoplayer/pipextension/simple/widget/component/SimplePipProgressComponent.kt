package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.pipextension.R
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPipProgressBinding
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleIControlComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoState
import xyz.doikki.videoplayer.pipextension.visible
import xyz.doikki.videoplayer.util.PlayerUtils

class SimplePipProgressComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), SimpleIControlComponent {

    private var wrapper: ControlWrapper? = null
    private val viewBinding = VideoLayoutPlayPipProgressBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        gone()
    }

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
        if (wrapper?.isPlaying == false) return
        if (isVisible) {
            visible()
            startAnimation(anim)
        } else {
            gone()
            startAnimation(anim)
        }
    }

    override fun onPlayStateChanged(state: SimpleVideoState) {
        when (state) {
            SimpleVideoState.IDLE, SimpleVideoState.PLAYBACK_COMPLETED -> {
                viewBinding.progress.progress = 0
                viewBinding.progress.secondaryProgress = 0
            }

            SimpleVideoState.PLAYING, SimpleVideoState.BUFFERING, SimpleVideoState.BUFFERED -> {
                wrapper?.startProgress()
            }

            else -> {
            }
        }
    }

    override fun setProgress(duration: Int, position: Int) {
        if (duration >= 0) {
            bringToFront()
            val value = position * 1.0 / duration * viewBinding.progress.max
            viewBinding.progress.progress = value.toInt()
            viewBinding.progress.visible()
        } else {
            viewBinding.progress.gone()
        }
        val percent = wrapper?.bufferedPercentage ?: 0
        if (percent >= 95) viewBinding.progress.secondaryProgress = viewBinding.progress.max
        else viewBinding.progress.secondaryProgress = percent * 10
        viewBinding.progressText.text = context.getString(
            R.string.video_progress_text,
            PlayerUtils.stringForTime(position),
            PlayerUtils.stringForTime(duration)
        )
    }

}