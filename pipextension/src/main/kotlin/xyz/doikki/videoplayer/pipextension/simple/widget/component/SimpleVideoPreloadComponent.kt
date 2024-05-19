package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPreloadBinding
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoComponent

internal class SimpleVideoPreloadComponent(context: Context) : SimpleVideoComponent(context) {

    init {
        addView(
            VideoLayoutPlayPreloadBinding
                .inflate(LayoutInflater.from(context), this, false).root
        )
        isVisible = false
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            bringToFront()
        }
    }

}