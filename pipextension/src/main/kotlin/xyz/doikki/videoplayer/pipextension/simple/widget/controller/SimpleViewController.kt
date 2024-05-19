package xyz.doikki.videoplayer.pipextension.simple.widget.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.animation.Animation
import androidx.core.view.isVisible
import xyz.doikki.videocontroller.component.CompleteView
import xyz.doikki.videocontroller.component.ErrorView
import xyz.doikki.videocontroller.component.GestureView
import xyz.doikki.videocontroller.component.PrepareView
import xyz.doikki.videocontroller.component.TitleView
import xyz.doikki.videocontroller.component.VodControlView
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayOperateViewBinding
import xyz.doikki.videoplayer.pipextension.isSingleVideoItem
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoController
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoState

internal class SimpleViewController(context: Context) : SimpleVideoController(context) {

    private val completeView = CompleteView(context)
    private val errorView = ErrorView(context)
    private val prepareView = PrepareView(context)
    private val titleView = TitleView(context)
    private val vodView = VodControlView(context)
    private val gestureView = GestureView(context)
    private val widgetView = SimpleWidgetView(context)

    fun addControlComponents(title: String) {
        prepareView.setClickStart()
        titleView.setTitle(title)
        addControlComponent(
            completeView,
            errorView,
            prepareView,
            titleView,
            vodView,
            gestureView,
            preloadComponent
        )
        addControlComponent(widgetView)
        setEnableInNormal(true)
    }

    override fun onBackPressed(): Boolean {
        if (controlWrapper?.isFullScreen == true) {
            return stopFullScreen()
        }
        return super.onBackPressed()
    }

    internal class SimpleWidgetView(context: Context) : SimpleVideoComponent(context) {

        private val viewBinding = VideoLayoutPlayOperateViewBinding
            .inflate(LayoutInflater.from(context), this, true)

        init {
            viewBinding.next.isVisible = !isSingleVideoItem
            viewBinding.prev.isVisible = !isSingleVideoItem

            viewBinding.rotation.setOnClickListener { VideoManager.refreshRotation() }
            viewBinding.scale.setOnClickListener { VideoManager.refreshScreenScale() }
            viewBinding.pip.setOnClickListener { VideoManager.entryPipMode() }
            viewBinding.next.setOnClickListener { VideoManager.videoPlayNext() }
            viewBinding.prev.setOnClickListener { VideoManager.videoPlayPrev() }
        }

        override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
            viewBinding.root.isVisible = isVisible
            startAnimation(anim)
        }

        override fun onPlayStateChanged(state: SimpleVideoState) {
            viewBinding.root.isVisible = false
        }

    }

}