package xyz.doikki.videoplayer.pipextension.controller

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.CompleteView
import xyz.doikki.videocontroller.component.ErrorView
import xyz.doikki.videocontroller.component.GestureView
import xyz.doikki.videocontroller.component.PrepareView
import xyz.doikki.videocontroller.component.TitleView
import xyz.doikki.videocontroller.component.VodControlView
import xyz.doikki.videoplayer.pipextension.listener.OnViewOrientationListener
import xyz.doikki.videoplayer.pipextension.view.gone
import xyz.doikki.videoplayer.pipextension.view.view.VideoOrientationView
import xyz.doikki.videoplayer.pipextension.view.visible
import xyz.doikki.videoplayer.player.VideoView

internal class VideoViewController @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : StandardVideoController(context, attrs, defStyleAttr) {

    fun setDefaultControlComponent(
        title: String,
        viewRotationListener: OnViewOrientationListener,
    ) {
        addDefaultControlComponent(title, false)
        addControlComponent(VideoOrientationView(context).registerListener(viewRotationListener))
        setEnableInNormal(true)
    }

    override fun addDefaultControlComponent(title: String, isLive: Boolean) {
        val completeView = CompleteView(context)
        val errorView = ErrorView(context)
        val prepareView = PrepareView(context)
        prepareView.setClickStart()
        val titleView = TitleView(context)
        titleView.setTitle(title)
        addControlComponent(completeView, errorView, prepareView, titleView)
        addControlComponent(FixStateVodControlView(context))
        addControlComponent(GestureView(context))
    }

    private class FixStateVodControlView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : VodControlView(context, attrs, defStyleAttr) {
        override fun onPlayStateChanged(playState: Int) {
            super.onPlayStateChanged(playState)
            if (playState == VideoView.STATE_IDLE
                || playState == VideoView.STATE_PLAYBACK_COMPLETED
                || playState == VideoView.STATE_START_ABORT
                || playState == VideoView.STATE_PREPARING
                || playState == VideoView.STATE_PREPARED
                || playState == VideoView.STATE_ERROR
            ) gone()
            else visible()
        }
    }

}