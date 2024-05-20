package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import android.app.Activity
import androidx.core.view.marginLeft
import xyz.doikki.videoplayer.pipextension.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimplePipController
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimpleViewController

internal class ControllerHelper(private val videoView: SimpleVideoView) {

    fun pipController() {
        videoView.removeViewFormParent()
        videoView.marginLeft
        val controller = SimplePipController(videoView.context)
        controller.addControlComponents()
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
    }

    fun viewController(activity: Activity, title: String) {
        videoView.removeViewFormParent()
        val controller = SimpleViewController(activity)
        controller.addControlComponents(title)
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
    }

}