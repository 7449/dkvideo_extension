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
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayOperateViewBinding
import xyz.doikki.videoplayer.pipextension.simple.config.SimpleLogger
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoComponent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoController
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoListener
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoState

internal class SimpleViewController(context: Context) : SimpleVideoController(context) {

    private val completeView = CompleteView(context)
    private val errorView = ErrorView(context)
    private val prepareView = PrepareView(context)
    private val titleView = TitleView(context)
    private val vodView = VodControlView(context)
    private val gestureView = GestureView(context)
    private val widgetView = SimpleWidgetView(context)

    fun addControlComponents(title: String, listener: SimpleVideoListener) {
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
        addControlComponent(widgetView.setListener(listener))
        setEnableInNormal(true)
    }

    override fun onBackPressed(): Boolean {
        if (controlWrapper?.isFullScreen == true) {
            return stopFullScreen()
        }
        return super.onBackPressed()
    }

    private class SimpleWidgetView(context: Context) : SimpleVideoComponent(context) {

        private val viewBinding = VideoLayoutPlayOperateViewBinding
            .inflate(LayoutInflater.from(context), this, true)

        private var listener: SimpleVideoListener? = null

        init {
            viewBinding.rotation.setOnClickListener { listener?.onClickRotation() }
            viewBinding.scale.setOnClickListener { listener?.onClickScale() }
            viewBinding.pip.setOnClickListener { listener?.onToPip() }
            viewBinding.next.setOnClickListener { listener?.onClickNext() }
            viewBinding.prev.setOnClickListener { listener?.onClickPrev() }
        }

        fun setListener(listener: SimpleVideoListener) = apply {
            this.listener = listener
            refreshUI()
        }

        private fun refreshUI() {
            viewBinding.next.isVisible = listener?.isSingleVideo == false
            viewBinding.prev.isVisible = listener?.isSingleVideo == false
        }

        override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {
            SimpleLogger.i("SimpleView onVisibilityChanged: $isVisible")
            viewBinding.root.isVisible = isVisible
            startAnimation(anim)
        }

        override fun onPlayStateChanged(state: SimpleVideoState) {
            SimpleLogger.i("SimpleView onPlayStateChanged: $state")
        }

        override fun onDetachedFromWindow() {
            listener = null
            super.onDetachedFromWindow()
        }

    }

}