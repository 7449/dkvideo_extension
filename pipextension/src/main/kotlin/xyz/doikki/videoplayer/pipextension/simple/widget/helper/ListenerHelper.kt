package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoContainerView
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.player.BaseVideoView
import xyz.doikki.videoplayer.player.BaseVideoView.SimpleOnStateChangeListener

class ListenerHelper(container: SimpleVideoContainerView, videoView: SimpleVideoView) {

    private var completed: ((view: SimpleVideoContainerView) -> Unit)? = null
    private var buffering: ((view: SimpleVideoContainerView) -> Unit)? = null
    private var buffered: ((view: SimpleVideoContainerView) -> Unit)? = null
    private var error: ((view: SimpleVideoContainerView) -> Unit)? = null
    private var playing: ((view: SimpleVideoContainerView) -> Unit)? = null
    private var videoSize: ((view: SimpleVideoContainerView, size: IntArray) -> Unit)? = null

    init {
        videoView.setOnVideoSizeChangedListener(object :
            SimpleVideoView.OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(size: IntArray) {
                videoSize?.invoke(container, size)
            }
        })
        videoView.setOnStateChangeListener(object : SimpleOnStateChangeListener() {
            override fun onPlayerStateChanged(playerState: Int) {
                videoView.post { videoSize?.invoke(container, videoView.videoSize) }
            }

            override fun onPlayStateChanged(playState: Int) {
                when (playState) {
                    BaseVideoView.STATE_PLAYBACK_COMPLETED -> {
                        completed?.invoke(container)
                    }

                    BaseVideoView.STATE_BUFFERING -> {
                        buffering?.invoke(container)
                    }

                    BaseVideoView.STATE_BUFFERED -> {
                        buffered?.invoke(container)
                    }

                    BaseVideoView.STATE_ERROR -> {
                        error?.invoke(container)
                    }

                    BaseVideoView.STATE_PLAYING -> {
                        playing?.invoke(container)
                    }
                }
            }
        })
    }

    fun completed(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.completed = action
    }

    fun buffering(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.buffering = action
    }

    fun buffered(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.buffered = action
    }

    fun error(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.error = action
    }

    fun videoSize(action: (view: SimpleVideoContainerView, size: IntArray) -> Unit) = apply {
        this.videoSize = action
    }

    fun playing(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        this.playing = action
    }

}