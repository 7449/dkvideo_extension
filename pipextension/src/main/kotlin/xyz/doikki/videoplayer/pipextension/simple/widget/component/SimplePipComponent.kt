package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPipOperateBinding
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleIControlComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoState
import xyz.doikki.videoplayer.pipextension.visible

class SimplePipComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), SimpleIControlComponent {

    private var wrapper: ControlWrapper? = null
    private val viewBinding = VideoLayoutPlayPipOperateBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        viewBinding.start.setOnClickListener { wrapper?.togglePlay() }
        viewBinding.close.setOnClickListener { VideoManager.closePipMode() }
        viewBinding.restore.setOnClickListener { VideoManager.pipComeBackActivity() }
        viewBinding.next.setOnClickListener { VideoManager.videoPlayNext() }
        viewBinding.prev.setOnClickListener { VideoManager.videoPlayPrev() }
        viewBinding.rotation.setOnClickListener { VideoManager.refreshRotation() }
        viewBinding.scale.setOnClickListener { VideoManager.refreshScreenScale() }
    }

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
        if (wrapper?.isPlaying == false) return
        if (isVisible) visibleViews()
        else goneViews()
        startAnimationViews(anim)
    }

    override fun onPlayStateChanged(state: SimpleVideoState) {
        viewBinding.start.isSelected = wrapper?.isPlaying ?: false
        if (state == SimpleVideoState.PAUSED || state == SimpleVideoState.IDLE) {
            visibleViews()
        } else {
            goneViews()
        }
        if (state == SimpleVideoState.PREPARING || state == SimpleVideoState.BUFFERING) {
            viewBinding.loading.visible()
        } else {
            viewBinding.loading.gone()
        }
    }

    private fun startAnimationViews(anim: Animation) {
        viewBinding.start.startAnimation(anim)
        viewBinding.close.startAnimation(anim)
        viewBinding.restore.startAnimation(anim)
        viewBinding.next.startAnimation(anim)
        viewBinding.prev.startAnimation(anim)
        viewBinding.rotation.startAnimation(anim)
        viewBinding.scale.startAnimation(anim)
    }

    private fun visibleViews() {
        playListView(true)
        viewBinding.start.visible()
        viewBinding.close.visible()
        viewBinding.restore.visible()
        viewBinding.rotation.visible()
        viewBinding.scale.visible()
    }

    private fun goneViews() {
        playListView(false)
        viewBinding.start.gone()
        viewBinding.close.gone()
        viewBinding.restore.gone()
        viewBinding.rotation.gone()
        viewBinding.scale.gone()
    }

    private fun playListView(isVisible: Boolean) {
        if (!VideoManager.isPlayList()) {
            viewBinding.next.gone()
            viewBinding.prev.gone()
            return
        }
        if (isVisible) {
            viewBinding.next.visible()
            viewBinding.prev.visible()
        } else {
            viewBinding.next.gone()
            viewBinding.prev.gone()
        }
    }

}