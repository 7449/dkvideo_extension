@file:SuppressLint("NotifyDataSetChanged")
//test video

package xyz.doikki.videoplayer.pipextension

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoActivity
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoListActivity
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoListAdapter

interface VideoUrlCallback {
    fun onRequestVideoUrl(action: (String) -> Unit)
}

data class SimpleVideoItem(
    val title: String,
    val urlCallback: VideoUrlCallback,
    val header: Map<String, String> = emptyMap(),
    val cover: Any? = null,
    val placeholder: Int? = null,
    val key: String = title,
    var select: Boolean = false,
) {
    companion object {
        fun create(
            title: String = "",
            url: String? = null,
            urlCallback: VideoUrlCallback? = null,
            header: Map<String, String> = emptyMap(),
            cover: Any? = null,
            placeholder: Int? = null,
            key: String = title,
            select: Boolean = true,
        ): SimpleVideoItem {
            return SimpleVideoItem(
                title = title,
                urlCallback = urlCallback ?: object : VideoUrlCallback {
                    override fun onRequestVideoUrl(action: (String) -> Unit) {
                        action.invoke(url.orEmpty())
                    }
                },
                header = header,
                cover = cover,
                placeholder = placeholder,
                key = key,
                select = select
            )
        }
    }
}

private object VideoItemManager {
    private val selectVideoItemIndex get() = videoItem.indexOfFirst { it.select }
    private val videoItemSize get() = videoItem.size
    val prevVideoItem get() = videoItem.subList(0, selectVideoItemIndex).lastOrNull()
    val nextVideoItem get() = videoItem.drop(selectVideoItemIndex + 1).firstOrNull()
    val isSingle get() = videoItemSize <= 1
    val selectVideoItem get() = videoItem.find { it.select }
    fun refreshItem(item: List<SimpleVideoItem>) {
        val newItem = item.map { it.copy() }
        videoItem.clear()
        videoItem.addAll(newItem)
    }

    fun refreshState(newItem: SimpleVideoItem) {
        videoItem.forEach { it.select = it.key == newItem.key }
        if (!isSingle) {
            simpleVideoAdapter.notifyDataSetChanged()
        }
    }
}

private val videoItem = arrayListOf<SimpleVideoItem>()
private val simpleVideoAdapter = SimpleVideoListAdapter(videoItem) { it.playVideoItem() }
private val simpleVideoListener = SimpleVideoPlayListener()

internal val isSingleVideoItem get() = VideoItemManager.isSingle
internal val selectVideoItem get() = VideoItemManager.selectVideoItem

private class SimpleVideoPlayListener : VideoListener {

    override fun onEntryPip() {
        val scanActivity = VideoManager.parentView?.activityOrNull
        if (scanActivity is SimpleVideoActivity) {
            scanActivity.switchPipMode()
        }
    }

    override fun onEntryActivity() {
        SimpleVideoPlayActivity.list(videoItem)
    }

    override fun onVideoPlayPrev() {
        if (isSingleVideoItem) return
        VideoItemManager.prevVideoItem.playVideoItem()
    }

    override fun onVideoPlayNext() {
        if (isSingleVideoItem) return
        VideoItemManager.nextVideoItem.playVideoItem()
    }

    override fun onVideoPlayError() {
        //ignore
    }
}

private fun SimpleVideoItem?.playVideoItem(parent: ViewGroup? = null) {
    val item = this ?: return
    VideoItemManager.refreshState(item)
    val viewGroup = VideoManager.parentView ?: parent ?: return
    VideoManager.attachParent(viewGroup, item.title)
    VideoManager.startVideo(item)
}

fun singlePipVideo(item: SimpleVideoItem) {
    listPipVideo(listOf(item.copy(select = true)))
}

fun listPipVideo(item: List<SimpleVideoItem>) {
    val context = VideoInitializer.appContext
    VideoItemManager.refreshItem(item)
    context.startActivity(Intent(context, SimpleVideoPermissionActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

internal class SimpleVideoPermissionActivity : FragmentActivity() {

    private val typeOverlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && isOverlayPermissions()) {
                switchPipMode()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VideoManager.setVideoListener(simpleVideoListener)
        switchPipMode()
    }

    override fun onDestroy() {
        super.onDestroy()
        typeOverlayLauncher.unregister()
    }

    private fun switchPipMode() {
        if (!isOverlayPermissions()) {
            typeOverlayLauncher.overlayLaunch()
        } else {
            val item = selectVideoItem ?: return
            VideoItemManager.refreshState(item)
            VideoManager.attachParent(title = item.title)
            VideoManager.startVideo(item)
        }
        finish()
    }

}

class SimpleVideoPlayActivity : SimpleVideoListActivity() {

    companion object {
        fun single(item: SimpleVideoItem) {
            list(listOf(item.copy(select = true)))
        }

        fun list(item: List<SimpleVideoItem>) {
            val context = VideoInitializer.appContext
            VideoItemManager.refreshItem(item)
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
        val videoItem = selectVideoItem ?: return
        if (!VideoManager.isPlaying) {
            videoItem.playVideoItem(viewBinding.video)
        } else if (VideoManager.isOverlay) {
            VideoManager.attachParent(viewBinding.video, videoItem.title, false)
        }
    }

    override fun createPlayListAdapter(): RecyclerView.Adapter<*>? {
        return if (isSingleVideoItem) null else simpleVideoAdapter
    }

}