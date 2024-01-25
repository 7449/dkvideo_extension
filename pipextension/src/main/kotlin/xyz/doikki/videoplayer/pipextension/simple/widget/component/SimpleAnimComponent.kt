package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayAnimBinding
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleIControlComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoState

class SimpleAnimComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), SimpleIControlComponent {

    init {
        addView(
            VideoLayoutPlayAnimBinding
                .inflate(LayoutInflater.from(context), this, false).root
        )
        gone()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            bringToFront()
        }
    }

    override fun onPlayStateChanged(state: SimpleVideoState) {
    }

    override fun attach(controlWrapper: ControlWrapper) {
    }

}