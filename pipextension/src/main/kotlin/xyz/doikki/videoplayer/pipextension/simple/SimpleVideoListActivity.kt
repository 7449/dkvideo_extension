package xyz.doikki.videoplayer.pipextension.simple

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.R
import xyz.doikki.videoplayer.pipextension.VideoManager

abstract class SimpleVideoListActivity :
    SimpleVideoActivity(R.layout.video_layout_play_list_activity) {

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.title = "视频播放"
        toolbar.setTitleTextColor(Color.WHITE)
        recyclerView.adapter = createPlayListAdapter()
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    protected abstract fun createPlayListAdapter(): RecyclerView.Adapter<*>

    override fun onResume() {
        super.onResume()
        VideoManager.isPlayList(true)
    }

}