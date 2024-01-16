package xyz.doikki.videoplayer.pipextension.view.helper

import xyz.doikki.videoplayer.pipextension.listener.OnVideoSizeChangedListener
import xyz.doikki.videoplayer.pipextension.view.AppCompatVideoView
import xyz.doikki.videoplayer.pipextension.view.view.VideoView
import xyz.doikki.videoplayer.player.BaseVideoView
import xyz.doikki.videoplayer.player.BaseVideoView.SimpleOnStateChangeListener

internal class VideoViewStateHelper(
    appCompatVideoView: AppCompatVideoView,
    videoView: VideoView,
) {

    private var completedAction: ((view: AppCompatVideoView) -> Unit)? = null
    private var bufferingAction: ((view: AppCompatVideoView) -> Unit)? = null
    private var bufferedAction: ((view: AppCompatVideoView) -> Unit)? = null
    private var errorAction: ((view: AppCompatVideoView) -> Unit)? = null
    private var playingAction: ((view: AppCompatVideoView) -> Unit)? = null
    private var videoSizeAction: ((view: AppCompatVideoView, videoSize: IntArray) -> Unit)? = null

    init {
        videoView.setOnVideoSizeChangedListener(object : OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(videoSize: IntArray) {
                videoSizeAction?.invoke(appCompatVideoView, videoSize)
            }
        })
        videoView.setOnStateChangeListener(object : SimpleOnStateChangeListener() {
            override fun onPlayerStateChanged(playerState: Int) {
                videoView.post { videoSizeAction?.invoke(appCompatVideoView, videoView.videoSize) }
            }

            override fun onPlayStateChanged(playState: Int) {
                when (playState) {
                    BaseVideoView.STATE_PLAYBACK_COMPLETED -> {
                        completedAction?.invoke(appCompatVideoView)
                    }

                    BaseVideoView.STATE_BUFFERING -> {
                        bufferingAction?.invoke(appCompatVideoView)
                    }

                    BaseVideoView.STATE_BUFFERED -> {
                        bufferedAction?.invoke(appCompatVideoView)
                    }

                    BaseVideoView.STATE_ERROR -> {
                        errorAction?.invoke(appCompatVideoView)
                    }

                    BaseVideoView.STATE_PLAYING -> {
                        playingAction?.invoke(appCompatVideoView)
                    }
                }
            }
        })
    }

    fun doOnPlayCompleted(action: (view: AppCompatVideoView) -> Unit) = apply {
        this.completedAction = action
    }

    fun doOnPlayBuffering(action: (view: AppCompatVideoView) -> Unit) = apply {
        this.bufferingAction = action
    }

    fun doOnPlayBuffered(action: (view: AppCompatVideoView) -> Unit) = apply {
        this.bufferedAction = action
    }

    fun doOnPlayError(action: (view: AppCompatVideoView) -> Unit) = apply {
        this.errorAction = action
    }

    fun doOnVideoSize(action: (view: AppCompatVideoView, size: IntArray) -> Unit) = apply {
        this.videoSizeAction = action
    }

    fun doOnPlaying(action: (view: AppCompatVideoView) -> Unit) = apply {
        this.playingAction = action
    }

}