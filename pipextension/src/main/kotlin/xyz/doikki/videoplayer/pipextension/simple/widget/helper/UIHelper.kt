package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import android.app.Activity
import androidx.cardview.widget.CardView
import xyz.doikki.videoplayer.pipextension.dp2px
import xyz.doikki.videoplayer.pipextension.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoContainerView
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimplePipController
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimpleViewController

class UIHelper(
    private val container: SimpleVideoContainerView,
    private val cardView: CardView,
    private val videoView: SimpleVideoView,
) {

    fun pipController() {
        container.removeViewFormParent()
        cardView.radius = 10f.dp2px().toFloat()
        val controller = SimplePipController(videoView.context)
        controller.addControlComponents()
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
    }

    fun viewController(activity: Activity, title: String) {
        container.removeViewFormParent()
        cardView.radius = 0f
        val controller = SimpleViewController(activity)
        controller.addControlComponents(title)
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
    }

}