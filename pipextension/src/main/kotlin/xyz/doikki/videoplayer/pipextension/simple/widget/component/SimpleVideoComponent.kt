package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayViewOperateBinding
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleIControlComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoState
import xyz.doikki.videoplayer.pipextension.visible

class SimpleVideoComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), SimpleIControlComponent {

    private var wrapper: ControlWrapper? = null
    private val viewBinding = VideoLayoutPlayViewOperateBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        gone()
        viewBinding.rotation.setOnClickListener { VideoManager.refreshRotation() }
        viewBinding.scale.setOnClickListener { VideoManager.refreshScreenScale() }
        viewBinding.pip.setOnClickListener { VideoManager.entryPipMode() }
        viewBinding.next.setOnClickListener { VideoManager.videoPlayNext() }
        viewBinding.prev.setOnClickListener { VideoManager.videoPlayPrev() }
    }

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
        if (isVisible) {
            visible()
            startAnimation(anim)
        } else {
            gone()
            startAnimation(anim)
        }
        if (VideoManager.isPlayList()) {
            viewBinding.next.visible()
            viewBinding.prev.visible()
        } else {
            viewBinding.next.gone()
            viewBinding.prev.gone()
        }
    }

    override fun onPlayStateChanged(state: SimpleVideoState) {
        gone()
    }

}