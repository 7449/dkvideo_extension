@file:SuppressLint("NotifyDataSetChanged")
//test video

package xyz.doikki.videoplayer.pipextension

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoActivity
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoListActivity
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoListAdapter

data class SimpleVideoItem(
    val title: String,
    val url: ((String) -> Unit) -> Unit,
    val header: Map<String, String> = emptyMap(),
    val cover: Any? = null,
    val placeholder: Int? = null,
    val key: String = title,
    var select: Boolean = false,
)

private val videoItem = arrayListOf<SimpleVideoItem>()
private val simpleVideoAdapter = SimpleVideoListAdapter(videoItem) { playVideoItem(it) }
private val simpleVideoListener = SimpleVideoPlayListener()

private val videoItemSize get() = videoItem.size
private val selectVideoItemIndex get() = videoItem.indexOfFirst { it.select }
private val prevVideoItem get() = videoItem.subList(0, selectVideoItemIndex).lastOrNull()
private val nextVideoItem get() = videoItem.drop(selectVideoItemIndex + 1).firstOrNull()

internal val isSingleVideoItem get() = videoItemSize <= 1
internal val selectVideoItem get() = if (videoItemSize > 1) videoItem.find { it.select } else videoItem.firstOrNull()

private class SimpleVideoPlayListener : VideoListener {
    override fun onEntryPip() {
        val scanActivity = VideoManager.parentView?.activityOrNull
        if (scanActivity is SimpleVideoActivity) {
            scanActivity.switchPipMode()
        }
    }

    override fun onEntryActivity() {
        if (videoItem.isEmpty()) return
        SimpleVideoPlayActivity.start(VideoInitializer.appContext, videoItem)
    }

    override fun onVideoPlayPrev() {
        if (isSingleVideoItem) return
        val item = prevVideoItem ?: return
        playVideoItem(item)
    }

    override fun onVideoPlayNext() {
        if (isSingleVideoItem) return
        val item = nextVideoItem ?: return
        playVideoItem(item)
    }

    override fun onVideoPlayError() {
        //ignore
    }
}

private fun playVideoItem(item: SimpleVideoItem, parent: ViewGroup? = null) {
    videoItem.forEach { it.select = it.key == item.key }
    if (!isSingleVideoItem) {
        simpleVideoAdapter.notifyDataSetChanged()
    }
    val viewGroup = VideoManager.parentView ?: parent ?: return
    VideoManager.attachParent(viewGroup, item.title)
    VideoManager.startVideo(item)
}

class SimpleVideoPlayActivity : SimpleVideoListActivity() {

    companion object {
        fun start(context: Context, item: List<SimpleVideoItem>) {
            val newItem = item.map { it.copy() }
            videoItem.clear()
            videoItem.addAll(newItem)
            context.startActivity(Intent(context, SimpleVideoPlayActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val color = Color.parseColor("#009688")
        viewBinding.toolbar.setBackgroundColor(color)
        viewBinding.toolbar.navigationIcon?.setTint(Color.WHITE)
        window.statusBarColor = color
        VideoManager.setVideoListener(simpleVideoListener)
        val videoItem = selectVideoItem ?: videoItem.firstOrNull() ?: return
        Log.e("Print", VideoManager.isOverlay.toString())
        if (!VideoManager.isPlaying) {
            playVideoItem(videoItem, viewBinding.video)
        } else if (VideoManager.isOverlay) {
            VideoManager.attachParent(viewBinding.video, videoItem.title, false)
        }
    }

    override fun createPlayListAdapter(): RecyclerView.Adapter<*>? {
        return if (isSingleVideoItem) null else simpleVideoAdapter
    }

}