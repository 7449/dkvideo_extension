package xyz.doikki.videoplayer.pipextension.simple.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoListener
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoPreload
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.ControllerHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.ListenerHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.UIHelper
import xyz.doikki.videoplayer.player.VideoView

internal class SimpleVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : VideoView(context, attrs, defStyleAttr), SimpleVideoPreload {

    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(size: IntArray)
    }

    private var videoSizeChangedListener: OnVideoSizeChangedListener? = null
    private val listenerHelper = ListenerHelper(this)
    private val controllerHelper = ControllerHelper(this)
    private val uiHelper = UIHelper(this)

    val url get() = mUrl.orEmpty()

    fun completed(action: (view: SimpleVideoView) -> Unit) = apply {
        listenerHelper.completed(action)
    }

    fun buffered(action: (view: SimpleVideoView) -> Unit) = apply {
        listenerHelper.buffered(action)
    }

    fun error(action: (view: SimpleVideoView) -> Unit) = apply {
        listenerHelper.error(action)
    }

    fun videoSize(action: (view: SimpleVideoView, size: IntArray) -> Unit) = apply {
        listenerHelper.videoSize(action)
    }

    fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        this.videoSizeChangedListener = listener
    }

    fun getPipVideo(listener: SimpleVideoListener) = apply {
        uiHelper.releaseRotation()
        uiHelper.releaseScreenScale()
        controllerHelper.pipController(listener)
    }

    fun getViewVideo(activity: Activity, title: String, listener: SimpleVideoListener) = apply {
        uiHelper.releaseRotation()
        uiHelper.releaseScreenScale()
        controllerHelper.viewController(activity, title, listener)
    }

    fun start(url: String, header: Map<String, String>) {
        revertSize()
        setUrl(url, header)
        start()
    }

    override fun release() {
        revertSize()
        setVideoController(null)
        uiHelper.releaseRotation()
        uiHelper.releaseScreenScale()
        super.release()
    }

    fun refreshRotation() {
        uiHelper.refreshRotation()
    }

    fun refreshScreenScale() {
        uiHelper.refreshScreenScale()
    }

    fun refreshVideoSize() {
        uiHelper.refreshVideoSize()
    }

    private fun revertSize() {
        mVideoSize = intArrayOf(0, 0)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        uiHelper.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        uiHelper.onDetachedFromWindow()
    }

    override fun onVideoSizeChanged(videoWidth: Int, videoHeight: Int) {
        super.onVideoSizeChanged(videoWidth, videoHeight)
        videoSizeChangedListener?.onVideoSizeChanged(videoSize)
    }

    override fun showVideoPreloadAnim() {
        val videoController = mVideoController
        if (videoController is SimpleVideoPreload) {
            videoController.showVideoPreloadAnim()
        }
    }

    override fun hideVideoPreloadAnim() {
        val videoController = mVideoController
        if (videoController is SimpleVideoPreload) {
            videoController.hideVideoPreloadAnim()
        }
    }

}