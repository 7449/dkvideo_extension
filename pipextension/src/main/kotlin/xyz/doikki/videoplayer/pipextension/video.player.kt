package xyz.doikki.videoplayer.pipextension

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import xyz.doikki.videoplayer.pipextension.simple.develop.activityOrNull
import xyz.doikki.videoplayer.pipextension.simple.develop.isOverlayPermissions
import xyz.doikki.videoplayer.pipextension.simple.develop.launchOverlay
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoUiActivity
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoUiAdapter

private val simpleVideoAdapter = SimpleVideoUiAdapter(VideoItemManager.allVideoItems) {
    it.startVideoItem()
}

private val simpleVideoListener = SimpleVideoPlayListener()

private class SimpleVideoPlayListener : OnVideoListener {

    override fun onEntryPip() {
        val scanActivity = VideoManager.parentView?.activityOrNull
        if (scanActivity is SimpleVideoUiActivity) {
            scanActivity.toPip()
        }
    }

    override fun onEntryActivity() {
        SimpleVideoPlayActivity.list(VideoItemManager.allVideoItems)
    }

    override fun onVideoPlayPrev() {
        if (VideoItemManager.isSingle) return
        VideoItemManager.prevVideoItem.startVideoItem()
    }

    override fun onVideoPlayNext() {
        if (VideoItemManager.isSingle) return
        VideoItemManager.nextVideoItem.startVideoItem()
    }

    override fun onVideoPlayError() {
        //ignore
    }
}

private fun SimpleVideoItem?.startVideoItem(parent: ViewGroup? = null) {
    val item = this ?: return
    VideoItemManager.refreshState(item) { simpleVideoAdapter.notifyDataSetChanged() }
    val viewGroup = VideoManager.parentView ?: parent
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

class SimpleVideoPlayActivity : SimpleVideoUiActivity() {

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
        val videoItem = VideoInitializer.listener.currentSelectVideoItem ?: return
        if (!VideoManager.isPlaying) {
            videoItem.startVideoItem(viewBinding.video)
        } else if (VideoManager.isOverlay) {
            VideoManager.attachParent(viewBinding.video, videoItem.title, false)
        }
    }

    override fun createPlayAdapter(): ListAdapter? {
        return if (VideoItemManager.isSingle) null else simpleVideoAdapter
    }

}

internal class SimpleVideoPermissionActivity : FragmentActivity() {

    private val typeOverlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && isOverlayPermissions()) {
                toPip()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VideoManager.setVideoListener(simpleVideoListener)
        toPip()
    }

    override fun onDestroy() {
        super.onDestroy()
        typeOverlayLauncher.unregister()
    }

    private fun toPip() {
        if (!isOverlayPermissions()) {
            typeOverlayLauncher.launchOverlay(packageName)
        } else {
            VideoInitializer.listener.currentSelectVideoItem?.startVideoItem()
        }
        finish()
    }

}