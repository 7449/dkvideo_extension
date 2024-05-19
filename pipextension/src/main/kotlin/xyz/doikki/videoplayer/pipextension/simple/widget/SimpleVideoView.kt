package xyz.doikki.videoplayer.pipextension.simple.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.pipextension.parentView
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.ControllerHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.ListenerHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.UIHelper
import xyz.doikki.videoplayer.player.VideoView

internal class SimpleVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : VideoView(context, attrs, defStyleAttr) {

    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(size: IntArray)
    }

    private var videoSizeChangedListener: OnVideoSizeChangedListener? = null
    private val listenerHelper = ListenerHelper(this)
    private val controllerHelper = ControllerHelper(this)
    private val uiHelper = UIHelper(this)

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

    fun getPipVideo() = apply {
        uiHelper.releaseRotation()
        uiHelper.releaseScreenScale()
        controllerHelper.pipController()
    }

    fun getViewVideo(activity: Activity, title: String) = apply {
        uiHelper.releaseRotation()
        uiHelper.releaseScreenScale()
        controllerHelper.viewController(activity, title)
    }

    fun showVideoPreloadAnim() {
        controllerHelper.showVideoPreloadAnim()
    }

    fun hideVideoPreloadAnim() {
        controllerHelper.hideVideoPreloadAnim()
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
        controllerHelper.release()
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        uiHelper.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        uiHelper.onDetachedFromWindow()
    }

    fun isOverlayParent(): Boolean {
        return parentView<SimpleVideoOverlayView>() != null
    }

    val url get() = mUrl.orEmpty()

    val isMp3 get() = url.lowercase().contains("mp3")

    val inspectSize get() = videoSize.first() != 0 && videoSize.last() != 0

    fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        this.videoSizeChangedListener = listener
    }

    override fun onVideoSizeChanged(videoWidth: Int, videoHeight: Int) {
        super.onVideoSizeChanged(videoWidth, videoHeight)
        videoSizeChangedListener?.onVideoSizeChanged(videoSize)
    }

    private fun revertSize() {
        mVideoSize = intArrayOf(0, 0)
    }

}