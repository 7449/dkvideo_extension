package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import android.annotation.SuppressLint
import android.app.Activity
import androidx.core.view.marginLeft
import xyz.doikki.videoplayer.pipextension.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoPreload
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimplePipController
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimpleViewController

@SuppressLint("UseCompatLoadingForDrawables")
internal class ControllerHelper(private val videoView: SimpleVideoView) {

    private var simpleVideoPreload: SimpleVideoPreload? = null

    fun pipController() {
        release()
        videoView.removeViewFormParent()
        videoView.marginLeft
        val controller = SimplePipController(videoView.context)
        controller.addControlComponents()
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
        simpleVideoPreload = controller
    }

    fun viewController(activity: Activity, title: String) {
        release()
        videoView.removeViewFormParent()
        val controller = SimpleViewController(activity)
        controller.addControlComponents(title)
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
        simpleVideoPreload = controller
    }

    fun showVideoPreloadAnim() {
        simpleVideoPreload?.showVideoPreloadAnim()
    }

    fun hideVideoPreloadAnim() {
        simpleVideoPreload?.hideVideoPreloadAnim()
    }

    fun release() {
        simpleVideoPreload = null
    }

}