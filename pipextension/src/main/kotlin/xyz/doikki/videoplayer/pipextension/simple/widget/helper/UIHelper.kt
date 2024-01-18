package xyz.doikki.videoplayer.pipextension.view.helper

import android.app.Activity
import androidx.cardview.widget.CardView
import xyz.doikki.videoplayer.pipextension.OnPipCompleteListener
import xyz.doikki.videoplayer.pipextension.OnPipErrorListener
import xyz.doikki.videoplayer.pipextension.OnPipOperateListener
import xyz.doikki.videoplayer.pipextension.OnViewOperateListener
import xyz.doikki.videoplayer.pipextension.PipController
import xyz.doikki.videoplayer.pipextension.ViewController
import xyz.doikki.videoplayer.pipextension.dp2px
import xyz.doikki.videoplayer.pipextension.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoContainerView

class VideoViewUiHelper(
    private val appCompatVideoView: SimpleVideoContainerView,
    private val cardView: CardView,
    private val videoView: SimpleVideoView,
) {

    fun pipControllerView(
        operateListener: OnPipOperateListener,
        errorListener: OnPipErrorListener,
        completeListener: OnPipCompleteListener,
    ) {
        appCompatVideoView.removeViewFormParent()
        cardView.radius = 10f.dp2px().toFloat()
        val pipController = PipController(videoView.context)
        pipController.setComponent(operateListener, errorListener, completeListener)
        videoView.setVideoController(pipController)
        pipController.setPlayState(videoView.currentPlayState)
        pipController.setPlayerState(videoView.currentPlayerState)
    }

    fun viewGroupControllerView(
        activity: Activity,
        title: String,
        operateListener: OnViewOperateListener,
    ) {
        appCompatVideoView.removeViewFormParent()
        cardView.radius = 0f
        val controller = ViewController(activity)
        controller.setComponent(title, operateListener)
        videoView.setVideoController(controller)
        controller.setPlayState(videoView.currentPlayState)
        controller.setPlayerState(videoView.currentPlayerState)
    }

}