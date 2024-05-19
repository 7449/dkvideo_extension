package xyz.doikki.videoplayer.pipextension

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.view.isVisible
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.OverlayHelper

internal object VideoManager {

    val isOverlay: Boolean get() = videoView.isOverlayParent() && OverlayHelper.isOverlay()
    val isPlaying: Boolean get() = videoView.isPlaying

    internal val parentView get() = videoView.parentView<ViewGroup>()
    private var videoListener: VideoListener? = null

    @SuppressLint("StaticFieldLeak")
    private val videoView = SimpleVideoView(VideoInitializer.appContext)
        .buffered { it.refreshVideoSize() }
        .error { videoPlayError() }
        .completed { videoPlayNext() }
        .videoSize { view, _ -> view.refreshVideoSize() }

    fun setVideoListener(listener: VideoListener) {
        videoListener = listener
    }

    fun startVideo(item: SimpleVideoItem) {
        videoView.showVideoPreloadAnim()
        item.url {
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
        OverlayHelper.get().addToWindow(videoView.getPipVideo())
        videoView.refreshVideoSize()
    }

    private fun attachPage(container: ViewGroup, title: String) {
        container.isVisible = true
        container.addView(videoView.getViewVideo(container.activity, title), videoMatchParams)
        videoView.refreshVideoSize()
        OverlayHelper.removeFromWindow()
    }

    private fun shutDown() {
        videoView.release()
        videoView.removeViewFormParent()
        OverlayHelper.release()
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

    fun closePipMode() {
        shutDown()
    }

    fun comeBackActivity() {
        videoListener?.onEntryActivity()
    }

    fun entryPipMode() {
        videoListener?.onEntryPip()
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

    internal fun refreshRotation() {
        videoView.refreshRotation()
    }

    internal fun refreshScreenScale() {
        videoView.refreshScreenScale()
    }

}