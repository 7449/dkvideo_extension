package xyz.doikki.videoplayer.pipextension.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.simple.SimpleVideoActivity

class SamplePlayActivity : SimpleVideoActivity(R.layout.sample_video_activity) {

    private val adapter = SampleVideoItemAdapter { playVideo(it, true) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.btn_list).setOnClickListener {
            startActivity(Intent(this, SamplePlayListActivity::class.java))
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
    }

    override fun onAttachVideoToView() {
        videoManager.attachView(findViewById<FrameLayout>(R.id.video), "")
    }

    private fun playVideo(url: String, forceView: Boolean = false) {
        val parent = if (!videoManager.isOverlay || forceView)
            findViewById<FrameLayout>(R.id.video)
        else null
        playVideo(url, url, url, parent)
    }

}