package xyz.doikki.videoplayer.pipextension.simple.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import xyz.doikki.videoplayer.pipextension.SimpleVideoItem
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayListItemBinding

internal class SimpleVideoListAdapter(
    private val item: List<SimpleVideoItem>,
    private val onClick: (SimpleVideoItem) -> Unit,
) : RecyclerView.Adapter<SimpleVideoListAdapter.ViewHolder>() {
    class ViewHolder(val binding: VideoLayoutPlayListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(VideoLayoutPlayListItemBinding.inflate(inflater, parent, false)).apply {
            itemView.setOnClickListener { onClick(item[bindingAdapterPosition]) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = item[position]
        holder.binding.text.text = model.title
        holder.binding.root.setBackgroundColor(if (model.select) Color.GRAY else Color.WHITE)
        holder.binding.cover.isVisible = model.cover != null
        holder.binding.cover.load(model.cover) {
            model.placeholder?.let { placeholder(it) }
            model.placeholder?.let { error(it) }
            crossfade(true)
            transformations(RoundedCornersTransformation(10f))
        }
    }

    override fun getItemCount(): Int = item.size
}