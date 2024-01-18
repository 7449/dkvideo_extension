package xyz.doikki.videoplayer.pipextension.simple.widget.controller

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.CompleteView
import xyz.doikki.videocontroller.component.ErrorView
import xyz.doikki.videocontroller.component.GestureView
import xyz.doikki.videocontroller.component.PrepareView
import xyz.doikki.videocontroller.component.TitleView
import xyz.doikki.videocontroller.component.VodControlView
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimpleVideoComponent

class SimpleViewController @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : StandardVideoController(context, attrs, defStyleAttr) {

    fun addControlComponents(title: String) {
        addDefaultControlComponent(title, false)
        addControlComponent(SimpleVideoComponent(context))
    }

    override fun addDefaultControlComponent(title: String, isLive: Boolean) {

        val completeView = CompleteView(context)
        val errorView = ErrorView(context)
        val prepareView = PrepareView(context)
        val titleView = TitleView(context)
        val vodView = VodControlView(context)
        val gestureView = GestureView(context)

        prepareView.setClickStart()
        titleView.setTitle(title)

        addControlComponent(completeView, errorView, prepareView, titleView, vodView, gestureView)
        setCanChangePosition(!isLive)
        setEnableInNormal(true)

    }

}