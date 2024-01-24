package xyz.doikki.videoplayer.pipextension.sample

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.simple.SimpleVideoListActivity

class SamplePlayListActivity : SimpleVideoListActivity() {

    private val adapter = SampleVideoItemAdapter {
        playVideo(it, true)
        closeDraw()
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