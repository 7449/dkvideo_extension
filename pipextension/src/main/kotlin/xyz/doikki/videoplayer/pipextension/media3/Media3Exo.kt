package xyz.doikki.videoplayer.pipextension.media3

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.util.EventLogger
import xyz.doikki.videoplayer.player.AbstractPlayer
import xyz.doikki.videoplayer.player.VideoViewManager

@UnstableApi
class Media3Exo(context: Context) : AbstractPlayer(), Player.Listener {

    private val application = context.applicationContext

    private var internalPlayer: ExoPlayer? = null
    private var mediaSource: MediaSource? = null
    private var playbackParameters: PlaybackParameters? = null
    private var isPreparing = false

    override fun initPlayer() {
        internalPlayer = ExoPlayer.Builder(application).build().apply {
            addListener(this@Media3Exo)
            if (VideoViewManager.getConfig().mIsEnableLog) {
                addAnalyticsListener(EventLogger("ExoPlayer"))
            }
        }
        setOptions()
    }

    override fun setDataSource(path: String, headers: MutableMap<String, String>?) {
        mediaSource = mediaSource(path, headers)
    }

    override fun setDataSource(fd: AssetFileDescriptor?) {
    }

    override fun start() {
        internalPlayer?.playWhenReady = true
    }

    override fun pause() {
        internalPlayer?.playWhenReady = false
    }

    override fun stop() {
        internalPlayer?.stop()
    }

    override fun prepareAsync() {
        val exoPlayer = internalPlayer ?: return
        if (mediaSource == null) return
        if (playbackParameters != null) {
            exoPlayer.playbackParameters = checkNotNull(playbackParameters)
        }
        isPreparing = true
        exoPlayer.setMediaSource(checkNotNull(mediaSource))
        exoPlayer.prepare()
    }

    override fun reset() {
        internalPlayer?.stop()
        internalPlayer?.clearMediaItems()
        internalPlayer?.setVideoSurface(null)
        isPreparing = false
    }

    override fun isPlaying(): Boolean {
        val exoPlayer = internalPlayer ?: return false
        return when (exoPlayer.playbackState) {
            Player.STATE_BUFFERING, Player.STATE_READY -> exoPlayer.playWhenReady
            Player.STATE_IDLE, Player.STATE_ENDED -> false
            else -> false
        }
    }

    override fun seekTo(time: Long) {
        internalPlayer?.seekTo(time)
    }

    override fun release() {
        internalPlayer?.removeListener(this)
        internalPlayer?.release()
        internalPlayer = null
        isPreparing = false
        playbackParameters = null
        mediaSource = null
    }

    override fun getCurrentPosition(): Long {
        return internalPlayer?.currentPosition ?: 0
    }

    override fun getDuration(): Long {
        return internalPlayer?.duration ?: 0
    }

    override fun getBufferedPercentage(): Int {
        return internalPlayer?.bufferedPercentage ?: 0
    }

    override fun setSurface(surface: Surface?) {
        internalPlayer?.setVideoSurface(surface)
    }

    override fun setDisplay(holder: SurfaceHolder?) {
        setSurface(holder?.surface)
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) {
        internalPlayer?.volume = (leftVolume + rightVolume) / 2
    }

    override fun setLooping(isLooping: Boolean) {
        internalPlayer?.repeatMode = if (isLooping) Player.REPEAT_MODE_ALL
        else Player.REPEAT_MODE_OFF
    }

    override fun setOptions() {
        internalPlayer?.playWhenReady = true
    }

    override fun setSpeed(speed: Float) {
        playbackParameters = playbackParameters?.withSpeed(speed) ?: PlaybackParameters(speed)
        internalPlayer?.playbackParameters = checkNotNull(playbackParameters)
    }

    override fun getSpeed(): Float {
        return playbackParameters?.speed ?: 1f
    }

    override fun getTcpSpeed(): Long {
        return 0
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        val eventListener = mPlayerEventListener ?: return
        if (isPreparing) {
            if (playbackState == Player.STATE_READY) {
                eventListener.onPrepared()
                eventListener.onInfo(MEDIA_INFO_RENDERING_START, 0)
                isPreparing = false
            }
            return
        }
        when (playbackState) {
            Player.STATE_BUFFERING -> eventListener.onInfo(
                MEDIA_INFO_BUFFERING_START,
                bufferedPercentage
            )

            Player.STATE_READY -> eventListener.onInfo(
                MEDIA_INFO_BUFFERING_END,
                bufferedPercentage
            )

            Player.STATE_ENDED -> eventListener.onCompletion()
            Player.STATE_IDLE -> {}
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        mPlayerEventListener?.onError()
    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        val eventListener = mPlayerEventListener ?: return
        eventListener.onVideoSizeChanged(videoSize.width, videoSize.height)
        if (videoSize.unappliedRotationDegrees > 0) {
            eventListener.onInfo(
                MEDIA_INFO_VIDEO_ROTATION_CHANGED,
                videoSize.unappliedRotationDegrees
            )
        }
    }

    private fun mediaSource(
        url: String,
        header: Map<String, String>? = null,
    ): MediaSource {
        val contentUri = Uri.parse(url)
        val contentType = contentType(url)

        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(header.orEmpty())
            .setAllowCrossProtocolRedirects(true)

        val factory = DefaultDataSource.Factory(application, dataSourceFactory)
        return when (contentType) {
            C.CONTENT_TYPE_DASH -> DashMediaSource.Factory(factory)
                .createMediaSource(MediaItem.fromUri(contentUri))

            C.CONTENT_TYPE_HLS -> HlsMediaSource.Factory(factory)
                .createMediaSource(MediaItem.fromUri(contentUri))

            else -> ProgressiveMediaSource.Factory(factory)
                .createMediaSource(MediaItem.fromUri(contentUri))
        }
    }

    private fun contentType(url: String): Int {
        return if (url.lowercase().contains("mpd")) {
            C.CONTENT_TYPE_DASH
        } else if (url.lowercase().contains("m3u8")) {
            C.CONTENT_TYPE_HLS
        } else {
            C.CONTENT_TYPE_OTHER
        }
    }

}