package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.player.BaseVideoView
import xyz.doikki.videoplayer.player.BaseVideoView.SimpleOnStateChangeListener

internal class ListenerHelper(private val videoView: SimpleVideoView) {

    private var completed: ((view: SimpleVideoView) -> Unit)? = null
    private var buffered: ((view: SimpleVideoView) -> Unit)? = null
    private var error: ((view: SimpleVideoView) -> Unit)? = null
    private var videoSize: ((view: SimpleVideoView, size: IntArray) -> Unit)? = null

    init {
        videoView.setOnVideoSizeChangedListener(object :
            SimpleVideoView.OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(size: IntArray) {
                videoSize?.invoke(videoView, size)
            }
        })
        videoView.setOnStateChangeListener(object : SimpleOnStateChangeListener() {
            override fun onPlayerStateChanged(playerState: Int) {
                videoView.post { videoSize?.invoke(videoView, videoView.videoSize) }
            }

            override fun onPlayStateChanged(playState: Int) {
                when (playState) {
                    BaseVideoView.STATE_PLAYBACK_COMPLETED -> completed?.invoke(videoView)
                    BaseVideoView.STATE_BUFFERED -> buffered?.invoke(videoView)
                    BaseVideoView.STATE_ERROR -> error?.invoke(videoView)
                }
            }
        })
    }

    fun completed(action: (view: SimpleVideoView) -> Unit) = apply {
        this.completed = action
    }

    fun buffered(action: (view: SimpleVideoView) -> Unit) = apply {
        this.buffered = action
    }

    fun error(action: (view: SimpleVideoView) -> Unit) = apply {
        this.error = action
    }

    fun videoSize(action: (view: SimpleVideoView, size: IntArray) -> Unit) = apply {
        this.videoSize = action
    }

}