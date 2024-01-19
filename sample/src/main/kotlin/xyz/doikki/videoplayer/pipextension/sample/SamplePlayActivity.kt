package xyz.doikki.videoplayer.pipextension.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.simple.SimpleVideoListActivity

class SamplePlayActivity : SimpleVideoListActivity(R.layout.sample_video_activity) {

    companion object {
        private val items = Urls.playList
    }

    private val adapter = SamplePlayListAdapter(
        onClick = {
            playVideo(it, true)
        },
        onLongClick = {
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private class SamplePlayListAdapter(
        private val onClick: (String) -> Unit,
        private val onLongClick: (String) -> Unit,
    ) : RecyclerView.Adapter<SamplePlayListAdapter.ViewHolder>() {

        private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val url: TextView = view.findViewById(R.id.url)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.sample_url_item, parent, false)
            ).apply {
                itemView.setOnClickListener {
                    onClick(items[adapterPosition])
                }
                itemView.setOnLongClickListener {
                    onLongClick(items[adapterPosition])
                    return@setOnLongClickListener true
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.url.text = items[position]
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

}