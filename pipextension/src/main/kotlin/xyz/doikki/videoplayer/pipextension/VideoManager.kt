package xyz.doikki.videoplayer.pipextension

import android.view.ViewGroup
import xyz.doikki.videoplayer.pipextension.view.AppCompatVideoView
import xyz.doikki.videoplayer.pipextension.view.VideoPipFloatView
import xyz.doikki.videoplayer.pipextension.view.isParentView
import xyz.doikki.videoplayer.pipextension.view.matchViewGroupParams
import xyz.doikki.videoplayer.pipextension.view.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.view.visible

class PipVideoManager :
    OnPipOperateListener,
    OnPipErrorListener,
    OnPipCompleteListener,
    OnViewOperateListener {

    companion object {
        val instance = PipVideoManager()
    }

    val isPipMode: Boolean get() = videoView.isParentView<VideoPipFloatView>() && floatView.isShow()
    val isPlaying: Boolean get() = videoView.isPlaying()
    val videoTag: String? get() = tag

    private var tag: String? = null
    private var pipManagerListener: OnPipManagerListener? = null
    private var isPlayList = true

    private val floatView = VideoPipFloatView(PipVideoInitializer.appContext)
    private val videoView = AppCompatVideoView(PipVideoInitializer.appContext)
        .doOnPlayBuffered { it.refreshVideoSize(VideoSizeChanged.BUFFERED) }
        .doOnVideoSize { view, _ -> view.refreshVideoSize(VideoSizeChanged.VIDEO_SIZE_CALLBACK) }
        .doOnPlayCompleted { onPipOperateClickNext() }
        .doOnPlayError { pipManagerListener?.onPipPlayError() }

    fun setPipManagerListener(listener: OnPipManagerListener): Boolean {
        if (pipManagerListener != listener) {
            pipManagerListener = listener
            return true
        }
        return false
    }

    fun isPlayList(playlist: Boolean) = apply {
        this.isPlayList = playlist
    }

    fun startVideo(url: String) {
        videoView.startVideo(url)
    }

    fun startVideo(container: ViewGroup?, tag: String, title: String, url: String) {
        attachVideo(container, tag, title)
        startVideo(url)
    }

    fun attachVideo(container: ViewGroup?, tag: String, title: String) {
        this.tag = tag
        videoView.release()
        if (container != null) {
            attachView(container, title)
        } else {
            attachWindow()
        }
    }

    fun attachWindow() {
        floatView.addToWindow(videoView.getPipVideo(this, this, this))
        videoView.refreshVideoSize(VideoSizeChanged.ATTACH_WINDOW)
    }

    fun attachView(container: ViewGroup, title: String) {
        container.visible()
        container.addView(
            videoView.getViewVideo(container.scanActivity(), title, this),
            matchViewGroupParams
        )
        videoView.refreshVideoSize(VideoSizeChanged.ATTACH_VIEW_GROUP)
        floatView.removeFromWindow()
    }

    fun showProgressView() {
        videoView.showProgressView()
    }

    fun showVideoView() {
        videoView.showVideoView()
    }

    fun shutDown() {
        videoView.release()
        videoView.removeViewFormParent()
        floatView.removeFromWindow()
        pipManagerListener = null
        tag = null
    }

    fun onPause() {
        if (isPipMode) return
        videoView.onPause()
    }

    fun onResume() {
        if (isPipMode) return
        videoView.onResume()
    }

    fun onDestroy() {
        if (isPipMode) return
        shutDown()
    }

    fun onBackPressed(): Boolean {
        if (isPipMode) return false
        return videoView.onBackPressed()
    }

    override fun onPipOperatePlayList(): Boolean {
        return isPlayList
    }

    /******************* listener method start *************************/
    override fun onPipOperateClickClose() {
        shutDown()
    }

    override fun onPipOperateClickRestore() {
        pipManagerListener?.onPipRestore()
    }

    override fun onPipOperateClickNext() {
        pipManagerListener?.onPipPlayNext()
    }

    override fun onPipOperateClickPrev() {
        pipManagerListener?.onPipPlayPrev()
    }

    override fun onPipOperateClickRotation() {
        videoView.refreshRotation()
    }

    override fun onPipOperateClickScreenScale() {
        videoView.refreshScreenScaleType()
    }

    override fun onPipErrorPlayList(): Boolean {
        return isPlayList
    }

    override fun onPipErrorClickClose() {
        onPipOperateClickClose()
    }

    override fun onPipErrorClickRestore() {
        onPipOperateClickRestore()
    }

    override fun onPipErrorClickNext() {
        onPipOperateClickNext()
    }

    override fun onPipErrorClickPrev() {
        onPipOperateClickPrev()
    }

    override fun onPipCompletePlayList(): Boolean {
        return isPlayList
    }

    override fun onPipCompleteClickClose() {
        onPipOperateClickClose()
    }

    override fun onPipCompleteClickRestore() {
        onPipOperateClickRestore()
    }

    override fun onPipCompleteClickNext() {
        onPipOperateClickNext()
    }

    override fun onPipCompleteClickPrev() {
        onPipOperateClickPrev()
    }

    override fun onViewOperateRotationClick() {
        onPipOperateClickRotation()
    }

    override fun onViewOperateScreenScaleClick() {
        onPipOperateClickScreenScale()
    }

    override fun onViewOperatePipClick() {
        pipManagerListener?.onPipEntry()
    }

    /******************* listener method end *************************/

}