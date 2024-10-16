package xyz.doikki.videoplayer.pipextension.simple.widget.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import androidx.core.view.isVisible
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayOperatePipBinding
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoController
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoListener
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoState

internal class SimplePipController(context: Context) : SimpleVideoController(context) {

    fun addControlComponents(listener: SimpleVideoListener) {
        addControlComponent(SimplePipComponent(context).setListener(listener))
        addControlComponent(preloadComponent)
    }

    private class SimplePipComponent(context: Context) : SimpleVideoComponent(context) {

        private val viewBinding = VideoLayoutPlayOperatePipBinding
            .inflate(LayoutInflater.from(context), this, true)

        private var listener: SimpleVideoListener? = null

        init {
            viewBinding.close.setOnClickListener { listener?.onClosePip() }
            viewBinding.comeBack.setOnClickListener { listener?.onComeBackActivity() }
            viewBinding.next.setOnClickListener { listener?.onClickNext() }
            viewBinding.prev.setOnClickListener { listener?.onClickPrev() }
            viewBinding.rotation.setOnClickListener { listener?.onClickRotation() }
            viewBinding.scale.setOnClickListener { listener?.onClickScale() }
            viewBinding.start.setOnClickListener { controlWrapper?.togglePlay() }
            viewBinding.replay.setOnClickListener { controlWrapper?.replay(true) }
            viewBinding.retry.setOnClickListener { controlWrapper?.replay(false) }
        }

        fun setListener(listener: SimpleVideoListener) = apply {
            this.listener = listener
            refreshUI()
        }

        private fun refreshUI() {
            viewBinding.next.isVisible = listener?.isSingleVideo == false
            viewBinding.prev.isVisible = listener?.isSingleVideo == false
        }

        override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
            viewBinding.control.isVisible = isVisible
            viewBinding.start.isVisible = isVisible
            viewBinding.control.startAnimation(anim)
        }

        override fun onPlayStateChanged(state: SimpleVideoState) {
            viewBinding.start.isSelected = controlWrapper?.isPlaying == true

            viewBinding.control.isVisible = state == SimpleVideoState.PAUSED
                    || state == SimpleVideoState.IDLE
                    || state == SimpleVideoState.PLAYBACK_COMPLETED

            viewBinding.start.isVisible = state == SimpleVideoState.PAUSED
                    || state == SimpleVideoState.IDLE

            viewBinding.loading.isVisible = state == SimpleVideoState.PREPARING
                    || state == SimpleVideoState.BUFFERING

            viewBinding.replay.isVisible = state == SimpleVideoState.PLAYBACK_COMPLETED
            viewBinding.retry.isVisible = state == SimpleVideoState.ERROR

            if (state == SimpleVideoState.IDLE || state == SimpleVideoState.PLAYBACK_COMPLETED) {
                viewBinding.progress.progress = 0
                viewBinding.progress.secondaryProgress = 0
            }
            if (state == SimpleVideoState.PLAYING || state == SimpleVideoState.BUFFERING || state == SimpleVideoState.BUFFERED) {
                controlWrapper?.startProgress()
            }
        }

        override fun setProgress(duration: Int, position: Int) {
            if (duration >= 0) {
                bringToFront()
                val value = position * 1.0 / duration * viewBinding.progress.max
                viewBinding.progress.progress = value.toInt()
            }
            val percent = controlWrapper?.bufferedPercentage ?: 0
            if (percent >= 95) viewBinding.progress.secondaryProgress = viewBinding.progress.max
            else viewBinding.progress.secondaryProgress = percent * 10
        }

        override fun onDetachedFromWindow() {
            listener = null
            super.onDetachedFromWindow()
        }

    }

}