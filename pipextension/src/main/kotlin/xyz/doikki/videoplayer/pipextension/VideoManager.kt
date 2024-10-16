package xyz.doikki.videoplayer.pipextension

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.view.isVisible
import xyz.doikki.videoplayer.pipextension.simple.develop.activity
import xyz.doikki.videoplayer.pipextension.simple.develop.activityOrNull
import xyz.doikki.videoplayer.pipextension.simple.develop.isOverlayParent
import xyz.doikki.videoplayer.pipextension.simple.develop.parentView
import xyz.doikki.videoplayer.pipextension.simple.develop.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.simple.develop.videoMatchParams
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.OverlayViewHelper

internal object VideoManager {

    val isOverlay: Boolean get() = videoView.isOverlayParent() && OverlayViewHelper.isOverlay()
    val isPlaying: Boolean get() = videoView.isPlaying

    val parentView get() = videoView.parentView<ViewGroup>()
    private var videoListener: OnVideoListener? = null

    @SuppressLint("StaticFieldLeak")
    private val videoView = SimpleVideoView(VideoInitializer.appContext)
        .buffered { it.refreshVideoSize() }
        .error { videoPlayError() }
        .completed { videoPlayNext() }
        .videoSize { view, _ -> view.refreshVideoSize() }

    fun setVideoListener(listener: OnVideoListener) {
        videoListener = listener
    }

    fun startVideo(item: SimpleVideoItem) {
        videoView.showVideoPreloadAnim()
        item.urlCallback.onRequestVideoUrl {
            videoView.hideVideoPreloadAnim()
            videoView.start(it, item.header)
        }
    }

    fun attachParent(container: ViewGroup? = null, title: String = "", release: Boolean = true) {
        if (release) {
            videoView.release()
        }
        if (container != null && container.activityOrNull != null) attachPage(container, title)
        else attachPip()
    }

    private fun attachPip() {
        OverlayViewHelper.get(VideoInitializer.appContext).addToWindow(
            videoView.getPipVideo(VideoInitializer.listener)
        )
        videoView.refreshVideoSize()
    }

    private fun attachPage(container: ViewGroup, title: String) {
        container.isVisible = true
        container.addView(
            videoView.getViewVideo(
                container.activity,
                title,
                VideoInitializer.listener
            ), videoMatchParams
        )
        videoView.refreshVideoSize()
        OverlayViewHelper.removeFromWindow()
    }

    private fun shutDown() {
        videoView.release()
        videoView.removeViewFormParent()
        OverlayViewHelper.release()
        videoListener = null
    }

    fun onPause() {
        if (isOverlay) return
        videoView.pause()
    }

    fun onResume() {
        if (isOverlay) return
        videoView.resume()
    }

    fun onDestroy() {
        if (isOverlay) return
        shutDown()
    }

    fun onBackPressed(): Boolean {
        if (isOverlay) return false
        return videoView.onBackPressed()
    }

    fun closePip() {
        shutDown()
    }

    fun toPip() {
        videoListener?.onEntryPip()
    }

    fun comeBackActivity() {
        videoListener?.onEntryActivity()
    }

    fun videoPlayNext() {
        videoListener?.onVideoPlayNext()
    }

    fun videoPlayPrev() {
        videoListener?.onVideoPlayPrev()
    }

    private fun videoPlayError() {
        videoListener?.onVideoPlayError()
    }

    fun refreshRotation() {
        videoView.refreshRotation()
    }

    fun refreshScreenScale() {
        videoView.refreshScreenScale()
    }

}