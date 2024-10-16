package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import android.app.Activity
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoListener
import xyz.doikki.videoplayer.pipextension.simple.develop.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimplePipController
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimpleViewController

internal class ControllerHelper(private val videoView: SimpleVideoView) {

    fun pipController(listener: SimpleVideoListener) {
        videoView.removeViewFormParent()
        val controller = SimplePipController(videoView.context)
        controller.addControlComponents(listener)
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
    }

    fun viewController(activity: Activity, title: String, listener: SimpleVideoListener) {
        videoView.removeViewFormParent()
        val controller = SimpleViewController(activity)
        controller.addControlComponents(title, listener)
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
    }

}