package xyz.doikki.videoplayer.pipextension.simple.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayContainerBinding
import xyz.doikki.videoplayer.pipextension.parentView
import xyz.doikki.videoplayer.pipextension.simple.develop.SourceVideoSize
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.ApiHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.ControllerHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.ListenerHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.UIHelper

class SimpleVideoContainerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding =
        VideoLayoutPlayContainerBinding.inflate(LayoutInflater.from(context), this, true)

    private val listenerHelper = ListenerHelper(this, viewBinding.videoView)
    private val apiHelper = ApiHelper(viewBinding.videoView)
    private val controllerHelper = ControllerHelper(this, viewBinding.videoView)
    private val uiHelper = UIHelper(this, viewBinding.videoView)

    fun completed(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        listenerHelper.completed(action)
    }

    fun buffering(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        listenerHelper.buffering(action)
    }

    fun buffered(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        listenerHelper.buffered(action)
    }

    fun error(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        listenerHelper.error(action)
    }

    fun playing(action: (view: SimpleVideoContainerView) -> Unit) = apply {
        listenerHelper.playing(action)
    }

    fun videoSize(action: (view: SimpleVideoContainerView, size: IntArray) -> Unit) = apply {
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

    fun startVideo(url: String, header: Map<String, String>) {
        apiHelper.startVideo(url, header)
    }

    fun isPlaying(): Boolean {
        return apiHelper.isPlaying()
    }

    fun release() {
        apiHelper.release()
        uiHelper.releaseRotation()
        uiHelper.releaseScreenScale()
    }

    fun onPause() {
        apiHelper.onPause()
    }

    fun onResume() {
        apiHelper.onResume()
    }

    fun onBackPressed(): Boolean {
        return apiHelper.onBackPressed()
    }

    fun showAnimView() {
        apiHelper.showAnimView()
    }

    fun showVideoView() {
        apiHelper.showVideoView()
    }

    fun refreshRotation() {
        uiHelper.refreshRotation()
    }

    fun refreshScreenScale() {
        uiHelper.refreshScreenScale()
    }

    fun refreshVideoSize(videoSizeChangedType: SourceVideoSize) {
        uiHelper.refreshVideoSize(videoSizeChangedType)
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

}