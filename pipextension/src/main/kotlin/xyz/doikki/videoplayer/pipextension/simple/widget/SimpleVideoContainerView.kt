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
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.ListenerHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.OriHelper
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.UIHelper

class SimpleVideoContainerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding =
        VideoLayoutPlayContainerBinding.inflate(LayoutInflater.from(context), this, true)

    private val listenerHelper = ListenerHelper(this, viewBinding.videoView)
    private val apiHelper = ApiHelper(viewBinding.progress, viewBinding.videoView)
    private val uiHelper = UIHelper(this, viewBinding.root, viewBinding.videoView)
    private val oriHelper = OriHelper(this, viewBinding.videoView)

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
        oriHelper.releaseRotation()
        oriHelper.releaseScreenScale()
        uiHelper.pipController()
    }

    fun getViewVideo(activity: Activity, title: String) = apply {
        oriHelper.releaseRotation()
        oriHelper.releaseScreenScale()
        uiHelper.viewController(activity, title)
    }

    fun startVideo(url: String) {
        apiHelper.startVideo(url)
    }

    fun isPlaying(): Boolean {
        return apiHelper.isPlaying()
    }

    fun release() {
        apiHelper.release()
        oriHelper.releaseRotation()
        oriHelper.releaseScreenScale()
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
        oriHelper.refreshRotation()
    }

    fun refreshScreenScale() {
        oriHelper.refreshScreenScale()
    }

    fun refreshVideoSize(videoSizeChangedType: SourceVideoSize) {
        oriHelper.refreshVideoSize(videoSizeChangedType)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        oriHelper.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        oriHelper.onDetachedFromWindow()
    }

    fun isOverlayParent(): Boolean {
        return parentView<SimpleVideoOverlayView>() != null
    }

}