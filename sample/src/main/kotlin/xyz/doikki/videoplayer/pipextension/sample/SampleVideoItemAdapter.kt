package xyz.doikki.videoplayer.pipextension.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SampleVideoItemAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<SampleVideoItemAdapter.ViewHolder>() {

    companion object {
        private val items = Urls.playList
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val url: TextView = view.findViewById(R.id.url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.sample_url_item, parent, false)
        ).apply {
            itemView.setOnClickListener { onClick(items[bindingAdapterPosition]) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.url.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}