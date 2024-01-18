package xyz.doikki.videoplayer.pipextension.view.helper

import android.view.View
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.visible

class VideoViewApiHelper(
    private val progressView: View,
    private val videoView: SimpleVideoView,
) {

    fun startVideo(url: String) {
        videoView.revertSize()
        videoView.setUrl(url)
        videoView.start()
    }

    fun isPlaying(): Boolean {
        return videoView.isPlaying
    }

    fun release() {
        videoView.revertSize()
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