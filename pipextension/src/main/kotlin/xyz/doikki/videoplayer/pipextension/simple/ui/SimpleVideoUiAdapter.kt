package xyz.doikki.videoplayer.pipextension.simple.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import xyz.doikki.videoplayer.pipextension.SimpleVideoItem
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayUiItemBinding

internal class SimpleVideoUiAdapter(
    private val item: List<SimpleVideoItem>,
    private val onClick: (SimpleVideoItem) -> Unit,
) : BaseAdapter() {

    override fun getCount(): Int {
        return item.size
    }

    override fun getItem(position: Int): SimpleVideoItem {
        return item[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder = if (convertView == null) {
            ViewHolder(
                VideoLayoutPlayUiItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ).apply {
                binding.root.tag = this
                binding.root.setOnClickListener { onClick(getItem(position)) }
            }
        } else {
            convertView.tag as ViewHolder
        }
        viewHolder.bind(getItem(position))
        return viewHolder.binding.root
    }

    class ViewHolder(val binding: VideoLayoutPlayUiItemBinding) {
        fun bind(item: SimpleVideoItem) {
            binding.text.text = item.title
            binding.root.setBackgroundColor(if (item.select) Color.GRAY else Color.WHITE)
        }
    }

}