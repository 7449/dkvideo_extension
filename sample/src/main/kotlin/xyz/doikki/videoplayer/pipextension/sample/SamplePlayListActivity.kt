package xyz.doikki.videoplayer.pipextension.sample

import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoListActivity

class SamplePlayListActivity : SimpleVideoListActivity() {

    private val adapter = SampleVideoItemAdapter {
        playVideo(it, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK
        toolbar.setBackgroundColor(Color.BLACK)
        toolbar.navigationIcon?.setTint(Color.WHITE)
    }

    override fun onAttachVideoToView() {
        videoManager.attachView(findViewById<FrameLayout>(R.id.video), "")
    }

    override fun createPlayListAdapter(): RecyclerView.Adapter<*> {
        return adapter
    }

    private fun playVideo(url: String, forceView: Boolean = false) {
        val parent = if (!videoManager.isOverlay || forceView)
            findViewById<FrameLayout>(R.id.video)
        else null
        playVideo(url, url, url, parent)
    }

}