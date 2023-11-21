package xyz.doikki.videoplayer.pipextension.view.pip

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPipProgressBinding
import xyz.doikki.videoplayer.pipextension.view.gone
import xyz.doikki.videoplayer.pipextension.view.visible
import xyz.doikki.videoplayer.player.VideoView
import xyz.doikki.videoplayer.util.PlayerUtils

internal class VideoPipProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IControlComponent {

    private var wrapper: ControlWrapper? = null
    private val viewBinding = VideoLayoutPlayPipProgressBinding
        .inflate(LayoutInflater.from(context), this, true)

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
        if (wrapper?.isPlaying == false) return
        if (isVisible) {
            viewBinding.bottomRoot.visible()
            viewBinding.progressText.visible()
        } else {
            viewBinding.bottomRoot.gone()
            viewBinding.progressText.gone()
        }
        viewBinding.bottomRoot.startAnimation(anim)
        viewBinding.progressText.startAnimation(anim)
    }

    override fun onPlayStateChanged(playState: Int) {
        if (playState == VideoView.STATE_PAUSED || playState == VideoView.STATE_IDLE) {
            viewBinding.bottomRoot.visible()
            viewBinding.progressText.visible()
        } else {
            viewBinding.bottomRoot.gone()
            viewBinding.progressText.gone()
        }
        when (playState) {
            VideoView.STATE_IDLE, VideoView.STATE_PLAYBACK_COMPLETED -> {
                viewBinding.progress.progress = 0
                viewBinding.progress.secondaryProgress = 0
            }

            VideoView.STATE_PLAYING -> wrapper?.startProgress()
            VideoView.STATE_BUFFERED -> wrapper?.startProgress()
            VideoView.STATE_BUFFERING -> wrapper?.stopProgress()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setProgress(duration: Int, position: Int) {
        if (duration >= 0) {
            viewBinding.progress.progress =
                (position * 1.0 / duration * viewBinding.progress.max).toInt()
            bringToFront()
            viewBinding.progress.visible()
        } else {
            viewBinding.progress.gone()
        }
        val percent = wrapper?.bufferedPercentage ?: 0
        if (percent >= 95) {
            viewBinding.progress.secondaryProgress = viewBinding.progress.max
        } else {
            viewBinding.progress.secondaryProgress = percent * 10
        }
        val positionTime = PlayerUtils.stringForTime(position)
        val totalTime = PlayerUtils.stringForTime(duration)
        viewBinding.progressText.text = "$positionTime/$totalTime"
    }

    override fun onPlayerStateChanged(playerState: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {}

}