package xyz.doikki.videoplayer.pipextension.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import xyz.doikki.videoplayer.pipextension.simple.SimpleVideoActivity

class SamplePlayActivity : SimpleVideoActivity(R.layout.sample_activity) {

    private val videoView by lazy { findViewById<FrameLayout>(R.id.video) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.play_list).setOnClickListener {
            startActivity(Intent(this, SamplePlayListActivity::class.java))
            finish()
        }
        findViewById<View>(R.id.play).setOnClickListener {
            playVideo(Urls.W3C_H5)
        }
    }

    override fun onResume() {
        super.onResume()
        videoManager.isPlayList(false)
    }

    override fun onAttachVideoToView() {
        videoManager.attachView(videoView, "")
    }

    private fun playVideo(url: String, isTouch: Boolean = false) {
        val rootView = if (!videoManager.isOverlay || isTouch)
            videoView
        else
            null
        playVideo(url, url, url, rootView)
    }

}