package xyz.doikki.videoplayer.pipextension.simple.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import xyz.doikki.videoplayer.pipextension.OnVideoListener
import xyz.doikki.videoplayer.pipextension.R
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayActivityMultipleItemBinding
import xyz.doikki.videoplayer.pipextension.initializer.VideoInitializer
import xyz.doikki.videoplayer.pipextension.scanActivityOrNull

data class MultiVideoItem(
    val cover: Any,
    val placeholder: Int,
    val title: String,
    val tag: String,
    val videoUrl: ((String) -> Unit) -> Unit,
    var select: Boolean,
)

private val List<MultiVideoItem>.prevItem: MultiVideoItem?
    get() = subList(0, indexOfFirst { it.select })
        .lastOrNull()

private val List<MultiVideoItem>.nextItem: MultiVideoItem?
    get() = drop(indexOfFirst { it.select } + 1)
        .firstOrNull()

private val multiVideoListener = MultiVideoPlayListListener()
private val multiVideoItems = arrayListOf<MultiVideoItem>()

class MultiVideoPlayItemAdapter(private val onClick: (MultiVideoItem) -> Unit) :
    RecyclerView.Adapter<MultiVideoPlayItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: VideoLayoutPlayActivityMultipleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VideoLayoutPlayActivityMultipleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener { onClick(multiVideoItems[bindingAdapterPosition]) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = multiVideoItems[position]
        holder.binding.text.text = model.title
        holder.binding.container.setBackgroundColor(if (model.select) Color.GRAY else Color.WHITE)
        holder.binding.cover.load(model.cover) {
            placeholder(model.placeholder)
            error(model.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return multiVideoItems.size
    }
}

private class MultiVideoPlayListListener : OnVideoListener {
    override fun onSwitchPipMode(parent: ViewGroup?) {
        val viewGroup = parent ?: return
        val scanActivity = viewGroup.scanActivityOrNull()
        if (scanActivity is SimpleVideoActivity) {
            scanActivity.switchPipMode()
        }
    }

    override fun onPipComeBackActivity(parent: ViewGroup?) {
        if (multiVideoItems.isEmpty()) return
        multiVideoFullScreen(multiVideoItems)
    }

    override fun onVideoPlayPrev(parent: ViewGroup?) {
        multiVideoItems.prevItem?.let { multiVideoPlayerClickItem(parent, it) }
    }

    override fun onVideoPlayNext(parent: ViewGroup?) {
        multiVideoItems.nextItem?.let { multiVideoPlayerClickItem(parent, it) }
    }

    override fun onVideoPlayError(parent: ViewGroup?) {
    }

}

@SuppressLint("NotifyDataSetChanged")
private fun multiVideoPlayerClickItem(parent: ViewGroup?, item: MultiVideoItem) {
    multiVideoItems.forEach { it.select = it.title == item.title }
    val viewGroup = parent ?: return
    val rootView = viewGroup.scanActivityOrNull()
        ?.findViewById<FrameLayout>(R.id.video)
    viewGroup.scanActivityOrNull()
        ?.findViewById<RecyclerView>(R.id.recyclerview)
        ?.adapter
        ?.notifyDataSetChanged()
    VideoManager.attachParent(rootView, item.tag, item.title)
    VideoManager.showAnimView()
    val showVideoAction: (String) -> Unit = {
        if (it.isNotBlank()) {
            VideoManager.showVideoView()
            VideoManager.startVideo(it)
        } else {
            VideoManager.shutDown()
        }
    }
    item.videoUrl(showVideoAction)
}

fun multiVideoFullScreen(itemList: List<MultiVideoItem>) {
    val selectItem = itemList.find { it.select } ?: multiVideoItems.find { it.select }
    val newList = itemList.map { it.copy(select = false) }
    multiVideoItems.clear()
    multiVideoItems.addAll(newList)
    multiVideoItems.forEach { it.select = it.title == selectItem?.title }
    val context = VideoInitializer.appContext
    context.startActivity(
        Intent(context, MultiVideoPlayActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

class MultiVideoPlayActivity : SimpleVideoListActivity() {

    private val adapter = MultiVideoPlayItemAdapter {
        multiVideoPlayerClickItem(findViewById(R.id.video), it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val color = Color.parseColor("#009688")
        toolbar.setBackgroundColor(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.navigationIcon?.setTint(Color.WHITE)
            window.statusBarColor = color
        }
        videoManager.setVideoListener(multiVideoListener)
        multiVideoItems.find { it.select }?.let {
            if (!videoManager.isPlaying) {
                multiVideoPlayerClickItem(findViewById(R.id.video), it)
            }
        }
    }

    override fun onAttachVideoToView() {
        val tag = videoManager.videoTag
        val model = multiVideoItems.find { it.tag == tag } ?: return
        videoManager.attachView(
            findViewById<FrameLayout>(R.id.video),
            model.title
        )
    }

    override fun createPlayListAdapter(): RecyclerView.Adapter<*> {
        return adapter
    }

}