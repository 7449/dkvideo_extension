package xyz.doikki.videoplayer.pipextension.simple.widget

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimplePipController
import xyz.doikki.videoplayer.pipextension.simple.widget.controller.SimpleViewController
import xyz.doikki.videoplayer.player.VideoView

class SimpleVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : VideoView(context, attrs, defStyleAttr) {

    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(size: IntArray)
    }

    private var videoSizeChangedListener: OnVideoSizeChangedListener? = null

    val url get() = mUrl.orEmpty()

    val isMp3 get() = url.lowercase().contains(".mp3")

    val inspectSize get() = videoSize.first() != 0 && videoSize.last() != 0

    fun revertSize() {
        mVideoSize = intArrayOf(0, 0)
    }

    fun showAnim() {
        val controller = mVideoController ?: return
        if (controller is SimplePipController) {
            controller.showAnim()
        } else if (controller is SimpleViewController) {
            controller.showAnim()
        }
    }

    fun hideAnim() {
        val controller = mVideoController ?: return
        if (controller is SimplePipController) {
            controller.hideAnim()
        } else if (controller is SimpleViewController) {
            controller.hideAnim()
        }
    }

    fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        this.videoSizeChangedListener = listener
    }

    override fun onVideoSizeChanged(videoWidth: Int, videoHeight: Int) {
        super.onVideoSizeChanged(videoWidth, videoHeight)
        videoSizeChangedListener?.onVideoSizeChanged(videoSize)
    }

}