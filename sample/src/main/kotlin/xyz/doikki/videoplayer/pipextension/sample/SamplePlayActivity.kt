package xyz.doikki.videoplayer.pipextension.sample

import android.os.Bundle
import android.widget.FrameLayout
import xyz.doikki.videoplayer.pipextension.simple.SimpleVideoActivity

class SampleActivity : SimpleVideoActivity(R.layout.sample_activity) {

    private val videoView by lazy { findViewById<FrameLayout>(R.id.video) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoManager.isPlayList(false)
        if (!isPipStartActivity()) {
            onPlayerClickItem(Urls.W3C_H5)
        }
    }

    override fun onResume() {
        super.onResume()
        videoManager.isPlayList(false)
    }

    override fun onPipComeBackActivity() {
        startActivity(intent)
    }

    override fun onVideoPlayPrev() {
    }

    override fun onVideoPlayNext() {
    }

    private fun onPlayerClickItem(url: String, isTouch: Boolean = false) {
        val rootView = if (!videoManager.isOverlay || isTouch) videoView
        else null
        videoManager.setVideoListener(this)
        videoManager.showAnimView()
        videoManager.attachVideo(rootView, url, url)
        videoManager.showVideoView()
        videoManager.startVideo(url)
    }

    private fun isPipStartActivity(): Boolean {
        if (!videoManager.isOverlay) return false
        videoManager.setVideoListener(this)
        videoManager.attachView(videoView, "")
        return true
    }

}