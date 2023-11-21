package xyz.doikki.videoplayer.pipextension.view.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayViewRotationBinding
import xyz.doikki.videoplayer.pipextension.listener.OnViewOrientationListener
import xyz.doikki.videoplayer.pipextension.view.gone
import xyz.doikki.videoplayer.pipextension.view.visible

internal class VideoOrientationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IControlComponent {

    private var wrapper: ControlWrapper? = null
    private var listener: OnViewOrientationListener? = null
    private val viewBinding = VideoLayoutPlayViewRotationBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        gone()
        viewBinding.rotation.setOnClickListener { listener?.onViewOrientationRotationClick() }
        viewBinding.scale.setOnClickListener { listener?.onViewOrientationScreenScaleClick() }
    }

    fun registerListener(listener: OnViewOrientationListener?) = apply {
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
//        viewBinding.scale.startAnimation(anim)
    }

    private fun visibleViews() {
        visible()
        viewBinding.rotation.visible()
//        viewBinding.scale.visible()
    }

    private fun goneViews() {
        gone()
        viewBinding.rotation.gone()
//        viewBinding.scale.gone()
    }

    override fun onPlayStateChanged(playState: Int) {}
    override fun setProgress(duration: Int, position: Int) {}
    override fun onPlayerStateChanged(playerState: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {}

}