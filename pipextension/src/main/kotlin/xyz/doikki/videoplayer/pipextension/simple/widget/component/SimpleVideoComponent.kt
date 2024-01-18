package xyz.doikki.videoplayer.pipextension.simple.widget.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.pipextension.OnViewOperateListener
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayViewOperateBinding
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.visible

class VideoOperateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr), IControlComponent {

    private var wrapper: ControlWrapper? = null
    private var listener: OnViewOperateListener? = null
    private val viewBinding = VideoLayoutPlayViewOperateBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        gone()
        viewBinding.rotation.setOnClickListener { listener?.onViewOperateRotationClick() }
        viewBinding.scale.setOnClickListener { listener?.onViewOperateScreenScaleClick() }
        viewBinding.pip.setOnClickListener { listener?.onViewOperatePipClick() }
    }

    fun registerListener(listener: OnViewOperateListener?) = apply {
        this.listener = listener
    }

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
        if (isVisible) visibleViews()
        else goneViews()
        startAnimationViews(anim)
    }

    private fun startAnimationViews(anim: Animation) {
        viewBinding.rotation.startAnimation(anim)
        viewBinding.pip.startAnimation(anim)
//        viewBinding.scale.startAnimation(anim)
    }

    private fun visibleViews() {
        visible()
        viewBinding.rotation.visible()
        viewBinding.pip.visible()
//        viewBinding.scale.visible()
    }

    private fun goneViews() {
        gone()
        viewBinding.rotation.gone()
        viewBinding.pip.gone()
//        viewBinding.scale.gone()
    }

    override fun onPlayStateChanged(playState: Int) {}
    override fun setProgress(duration: Int, position: Int) {}
    override fun onPlayerStateChanged(playerState: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {}

}