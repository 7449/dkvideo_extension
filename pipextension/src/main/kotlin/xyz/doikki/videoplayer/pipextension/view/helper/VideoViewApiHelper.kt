package xyz.doikki.videoplayer.pipextension.view.helper

import android.view.View
import xyz.doikki.videoplayer.pipextension.view.gone
import xyz.doikki.videoplayer.pipextension.view.view.VideoView
import xyz.doikki.videoplayer.pipextension.view.visible

internal class VideoViewApiHelper(
    private val progressView: View,
    private val videoView: VideoView,
) {

    fun startVideo(url: String) {
        videoView.resetVideoSize()
        videoView.setUrl(url)
        videoView.start()
    }

    fun isPlaying(): Boolean {
        return videoView.isPlaying
    }

    fun release() {
        videoView.resetVideoSize()
        videoView.setVideoController(null)
        videoView.release()
    }

    fun onPause() {
        videoView.pause()
    }

    fun onResume() {
        videoView.resume()
    }

    fun onBackPressed(): Boolean {
        return videoView.onBackPressed()
    }

    fun showProgressView() {
        videoView.gone()
        progressView.visible()
    }

    fun showVideoView() {
        videoView.visible()
        progressView.gone()
    }

}