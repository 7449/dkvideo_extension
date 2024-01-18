package xyz.doikki.videoplayer.pipextension.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.doikki.videoplayer.pipextension.simple.SimpleVideoActivity

class SamplePlayListActivity : SimpleVideoActivity(R.layout.sample_list_activity) {

    private val adapter = SamplePlayListAdapter {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
    }

    override fun onAttachVideoToView() {
    }

    override fun onResume() {
        super.onResume()
        videoManager.isPlayList(true)
    }

    override fun onPipComeBackActivity() {
    }

    override fun onVideoPlayPrev() {
    }

    override fun onVideoPlayNext() {
    }

    private class SamplePlayListAdapter(private val onClick: (String) -> Unit) :
        RecyclerView.Adapter<SamplePlayListAdapter.ViewHolder>() {

        private val arrays = Urls.playList

        private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val url: TextView = view.findViewById(R.id.url)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.sample_list_item, parent, false)
            ).apply {
                itemView.setOnClickListener {
                    onClick(arrays[adapterPosition])
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.url.text = arrays[position]
        }

        override fun getItemCount(): Int {
            return arrays.size
        }
    }

}