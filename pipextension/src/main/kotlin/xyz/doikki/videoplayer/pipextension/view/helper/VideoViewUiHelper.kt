package xyz.doikki.videoplayer.pipextension.view.helper

import android.app.Activity
import androidx.cardview.widget.CardView
import xyz.doikki.videoplayer.pipextension.controller.VideoPipController
import xyz.doikki.videoplayer.pipextension.controller.VideoViewController
import xyz.doikki.videoplayer.pipextension.dp2px
import xyz.doikki.videoplayer.pipextension.listener.OnPipCompleteListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipErrorListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipOperateListener
import xyz.doikki.videoplayer.pipextension.listener.OnViewOrientationListener
import xyz.doikki.videoplayer.pipextension.view.AppCompatVideoView
import xyz.doikki.videoplayer.pipextension.view.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.view.view.VideoView

internal class VideoViewUiHelper(
    private val appCompatVideoView: AppCompatVideoView,
    private val cardView: CardView,
    private val videoView: VideoView
) {

    fun pipControllerView(
        operateListener: OnPipOperateListener,
        errorListener: OnPipErrorListener,
        completeListener: OnPipCompleteListener
    ) {
        appCompatVideoView.removeViewFormParent()
        cardView.radius = 10f.dp2px()
        val pipController = VideoPipController(videoView.context)
        pipController.setDefaultControlComponent(operateListener, errorListener, completeListener)
        videoView.setVideoController(pipController)
        pipController.setPlayState(videoView.currentPlayState)
        pipController.setPlayerState(videoView.currentPlayerState)
    }

    fun viewGroupControllerView(
        activity: Activity,
        name: String,
        viewRotationListener: OnViewOrientationListener
    ) {
        appCompatVideoView.removeViewFormParent()
        cardView.radius = 0f
        val controller = VideoViewController(activity)
        controller.setDefaultControlComponent(name, viewRotationListener)
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
    }

}