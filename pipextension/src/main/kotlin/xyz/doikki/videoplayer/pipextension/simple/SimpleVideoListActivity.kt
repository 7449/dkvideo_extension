package xyz.doikki.videoplayer.pipextension.simple

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.postDelayed
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.R
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.fullScreen

abstract class SimpleVideoListActivity :
    SimpleVideoActivity(R.layout.video_layout_play_list_activity) {

    private val handler = Handler(Looper.getMainLooper())
    private val draw by lazy { findViewById<DrawerLayout>(R.id.draw) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        findViewById<View>(R.id.draw_open).setOnClickListener { draw.openDrawer(GravityCompat.END) }
        recyclerView.adapter = createPlayListAdapter()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handler.postDelayed(200) { fullScreen() }
    }

    override fun onBackPressedCallback() {
        if (draw.isDrawerOpen(GravityCompat.END)) {
            closeDraw()
        } else {
            super.onBackPressedCallback()
        }
    }

    protected fun closeDraw() {
        draw.closeDrawers()
    }

    protected abstract fun createPlayListAdapter(): RecyclerView.Adapter<*>

    override fun onResume() {
        super.onResume()
        VideoManager.isPlayList(true)
    }

}