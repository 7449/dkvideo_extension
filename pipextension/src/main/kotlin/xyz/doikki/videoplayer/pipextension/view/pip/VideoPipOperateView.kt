package xyz.doikki.videoplayer.pipextension.view.pip

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPipOperateBinding
import xyz.doikki.videoplayer.pipextension.listener.OnPipOperateListener
import xyz.doikki.videoplayer.pipextension.view.gone
import xyz.doikki.videoplayer.pipextension.view.visible
import xyz.doikki.videoplayer.player.VideoView

internal class VideoPipOperateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IControlComponent {

    private var wrapper: ControlWrapper? = null
    private var listener: OnPipOperateListener? = null
    private val viewBinding = VideoLayoutPlayPipOperateBinding
        .inflate(LayoutInflater.from(context), this, true)

    init {
        viewBinding.start.setOnClickListener { wrapper?.togglePlay() }
        viewBinding.close.setOnClickListener { listener?.onPipOperateClickClose() }
        viewBinding.restore.setOnClickListener { listener?.onPipOperateClickRestore() }
        viewBinding.next.setOnClickListener { listener?.onPipOperateClickNext() }
        viewBinding.prev.setOnClickListener { listener?.onPipOperateClickPrev() }
        viewBinding.rotation.setOnClickListener { listener?.onPipOperateClickRotation() }
        viewBinding.scale.setOnClickListener { listener?.onPipOperateClickScreenScale() }
    }

    fun registerListener(listener: OnPipOperateListener?) = apply {
        this.listener = listener
    }

    override fun attach(controlWrapper: ControlWrapper) {
        wrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
        if (wrapper?.isPlaying == false) return
        if (isVisible) visibleViews()
        else goneViews()
        startAnimationViews(anim)
    }

    private fun startAnimationViews(anim: Animation) {
        viewBinding.start.startAnimation(anim)
        viewBinding.close.startAnimation(anim)
        viewBinding.restore.startAnimation(anim)
        viewBinding.next.startAnimation(anim)
        viewBinding.prev.startAnimation(anim)
        viewBinding.rotation.startAnimation(anim)
//        viewBinding.scale.startAnimation(anim)
    }

    private fun visibleViews() {
        viewBinding.start.visible()
        viewBinding.close.visible()
        viewBinding.restore.visible()
        viewBinding.next.visible()
        viewBinding.prev.visible()
        viewBinding.rotation.visible()
//        viewBinding.scale.visible()
    }

    private fun goneViews() {
        viewBinding.start.gone()
        viewBinding.close.gone()
        viewBinding.restore.gone()
        viewBinding.next.gone()
        viewBinding.prev.gone()
        viewBinding.rotation.gone()
//        viewBinding.scale.gone()
    }

    override fun onPlayStateChanged(playState: Int) {
        viewBinding.start.isSelected = wrapper?.isPlaying ?: false
        if (playState == VideoView.STATE_PAUSED || playState == VideoView.STATE_IDLE) visibleViews()
        else goneViews()
        if (playState == VideoView.STATE_PREPARING || playState == VideoView.STATE_BUFFERING) viewBinding.loading.visible()
        else viewBinding.loading.gone()
    }

    override fun setProgress(duration: Int, position: Int) {}
    override fun onPlayerStateChanged(playerState: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {}

}