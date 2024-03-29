package xyz.doikki.videoplayer.pipextension

import android.view.ViewGroup
import xyz.doikki.videoplayer.pipextension.initializer.VideoInitializer
import xyz.doikki.videoplayer.pipextension.simple.develop.SourceVideoSize
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoContainerView
import xyz.doikki.videoplayer.pipextension.simple.widget.helper.OverlayHelper

object VideoManager {

    val isOverlay: Boolean get() = videoView.isOverlayParent() && OverlayHelper.isOverlay()
    val isPlaying: Boolean get() = videoView.isPlaying()
    val videoTag: String? get() = tag

    private var tag: String? = null
    private var videoListener: OnVideoListener? = null
    private var isPlayList = true
    private var isPip = true

    private val videoView = SimpleVideoContainerView(VideoInitializer.appContext)
        .buffered { it.refreshVideoSize(SourceVideoSize.BUFFERED) }
        .videoSize { view, _ -> view.refreshVideoSize(SourceVideoSize.VIDEO_SIZE) }
        .error { videoPlayError() }
        .completed {
            if (isPlayList()) {
                videoPlayNext()
            }
        }

    fun setVideoListener(listener: OnVideoListener) {
        videoListener = null
        videoListener = listener
    }

    fun isPlayList(playlist: Boolean) = apply {
        this.isPlayList = playlist
    }

    fun isPip(pip: Boolean) = apply {
        this.isPip = pip
    }

    fun startVideo(url: String) {
        videoView.startVideo(url)
    }

    fun startVideo(container: ViewGroup?, tag: String, title: String, url: String) {
        attachParent(container, tag, title)
        startVideo(url)
    }

    fun attachParent(container: ViewGroup?, tag: String, title: String) {
        this.tag = tag
        videoView.release()
        if (container != null) {
            attachView(container, title)
        } else {
            attachWindow()
        }
    }

    fun attachWindow() {
        OverlayHelper.get().addToWindow(videoView.getPipVideo())
        videoView.refreshVideoSize(SourceVideoSize.ATTACH_WINDOW)
    }

    fun attachView(container: ViewGroup, title: String) {
        container.visible()
        container.addView(
            videoView.getViewVideo(container.scanActivity(), title),
            videoMatchParams
        )
        videoView.refreshVideoSize(SourceVideoSize.ATTACH_VIEW)
        OverlayHelper.removeFromWindow()
    }

    fun showAnimView() {
        videoView.showAnimView()
    }

    fun showVideoView() {
        videoView.showVideoView()
    }

    fun shutDown() {
        videoView.release()
        videoView.removeViewFormParent()
        OverlayHelper.release()
        videoListener = null
        tag = null
    }

    fun onPause() {
        if (isOverlay) return
        videoView.onPause()
    }

    fun onResume() {
        if (isOverlay) return
        videoView.onResume()
    }

    fun onDestroy() {
        if (isOverlay) return
        shutDown()
    }

    fun onBackPressed(): Boolean {
        if (isOverlay) return false
        return videoView.onBackPressed()
    }

    internal fun closePipMode() {
        shutDown()
    }

    internal fun pipComeBackActivity() {
        videoListener?.onPipComeBackActivity(videoView.parentView())
    }

    internal fun entryPipMode() {
        videoListener?.onSwitchPipMode(videoView.parentView())
    }

    internal fun videoPlayNext() {
        videoListener?.onVideoPlayNext(videoView.parentView())
    }

    internal fun videoPlayPrev() {
        videoListener?.onVideoPlayPrev(videoView.parentView())
    }

    internal fun videoPlayError() {
        videoListener?.onVideoPlayError(videoView.parentView())
    }

    internal fun refreshRotation() {
        videoView.refreshRotation()
    }

    internal fun refreshScreenScale() {
        videoView.refreshScreenScale()
    }

    internal fun isPlayList(): Boolean {
        return isPlayList
    }

    internal fun isPip(): Boolean {
        return isPip
    }

}