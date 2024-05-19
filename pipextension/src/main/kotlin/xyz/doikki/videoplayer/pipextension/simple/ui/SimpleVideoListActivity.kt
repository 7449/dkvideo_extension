package xyz.doikki.videoplayer.pipextension.simple.ui

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.R
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayListPlayBinding

abstract class SimpleVideoListActivity : SimpleVideoActivity(R.layout.video_layout_play_list_play) {

    protected val viewBinding by lazy {
        VideoLayoutPlayListPlayBinding.bind(
            findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.toolbar.title = createToolbarTitle()
        viewBinding.toolbar.setTitleTextColor(Color.WHITE)
        createPlayListAdapter()?.let { viewBinding.recyclerview.adapter = it }
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewBinding.toolbar.isVisible = isShowToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewBinding.recyclerview.adapter = null
        super.onDestroy()
    }

    protected abstract fun createPlayListAdapter(): RecyclerView.Adapter<*>?

    protected open fun isShowToolbar(): Boolean {
        return true
    }

    protected open fun createToolbarTitle(): String {
        return "视频播放"
    }

}