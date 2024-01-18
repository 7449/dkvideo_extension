package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoContainerView
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.player.BaseVideoView
import xyz.doikki.videoplayer.player.BaseVideoView.SimpleOnStateChangeListener

class StateHelper(container: SimpleVideoContainerView, videoView: SimpleVideoView) {

    private var completedAction: ((container: SimpleVideoContainerView) -> Unit)? = null
    private var bufferingAction: ((container: SimpleVideoContainerView) -> Unit)? = null
    private var bufferedAction: ((container: SimpleVideoContainerView) -> Unit)? = null
    private var errorAction: ((container: SimpleVideoContainerView) -> Unit)? = null
    private var playingAction: ((container: SimpleVideoContainerView) -> Unit)? = null
    private var videoSizeAction: ((container: SimpleVideoContainerView, size: IntArray) -> Unit)? =
        null

    init {
        videoView.setOnVideoSizeChangedListener(object :
            SimpleVideoView.OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(size: IntArray) {
                videoSizeAction?.invoke(container, size)
            }
        })
        videoView.setOnStateChangeListener(object : SimpleOnStateChangeListener() {
            override fun onPlayerStateChanged(playerState: Int) {
                videoView.post { videoSizeAction?.invoke(container, videoView.videoSize) }
            }

            override fun onPlayStateChanged(playState: Int) {
                when (playState) {
                    BaseVideoView.STATE_PLAYBACK_COMPLETED -> {
                        completedAction?.invoke(container)
                    }

                    BaseVideoView.STATE_BUFFERING -> {
                        bufferingAction?.invoke(container)
                    }

                    BaseVideoView.STATE_BUFFERED -> {
                        bufferedAction?.invoke(container)
                    }

                    BaseVideoView.STATE_ERROR -> {
                        errorAction?.invoke(container)
                    }

                    BaseVideoView.STATE_PLAYING -> {
                        playingAction?.invoke(container)
                    }
                }
            }
        })
    }

    fun doOnPlayCompleted(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.completedAction = action
    }

    fun doOnPlayBuffering(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.bufferingAction = action
    }

    fun doOnPlayBuffered(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.bufferedAction = action
    }

    fun doOnPlayError(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.errorAction = action
    }

    fun doOnVideoSize(action: (view: SimpleVideoContainerView, size: IntArray) -> Unit) = apply {
        this.videoSizeAction = action
    }

    fun doOnPlaying(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.playingAction = action
    }

}