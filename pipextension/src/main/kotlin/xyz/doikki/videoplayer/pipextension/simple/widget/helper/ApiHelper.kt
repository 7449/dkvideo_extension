package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView

class ApiHelper(private val videoView: SimpleVideoView) {

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

    fun showAnimView() {
        videoView.showAnim()
    }

    fun showVideoView() {
        videoView.hideAnim()
    }

}