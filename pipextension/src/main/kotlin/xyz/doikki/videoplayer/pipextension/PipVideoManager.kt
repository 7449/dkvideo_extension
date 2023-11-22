package xyz.doikki.videoplayer.pipextension

import android.view.ViewGroup
import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory
import xyz.doikki.videoplayer.pipextension.listener.OnPipCompleteListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipErrorListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipOperateListener
import xyz.doikki.videoplayer.pipextension.listener.OnViewOrientationListener
import xyz.doikki.videoplayer.pipextension.types.VideoSizeChangedType
import xyz.doikki.videoplayer.pipextension.view.AppCompatVideoView
import xyz.doikki.videoplayer.pipextension.view.VideoPipFloatView
import xyz.doikki.videoplayer.pipextension.view.isParentView
import xyz.doikki.videoplayer.pipextension.view.matchViewGroupParams
import xyz.doikki.videoplayer.pipextension.view.removeViewFormParent
import xyz.doikki.videoplayer.pipextension.view.visible
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager
import xyz.doikki.videoplayer.util.PlayerUtils

class PipVideoManager : OnPipOperateListener,
    OnPipErrorListener,
    OnPipCompleteListener,
    OnViewOrientationListener {

    companion object {
        val instance = PipVideoManager()
    }

    init {
        VideoViewManager.setConfig(
            VideoViewConfig.newBuilder()
                .setPlayerFactory(ExoMediaPlayerFactory.create())
                .setProgressManager(PlayerProgressManager())
                .build()
        )
    }

    val isPipFloatView: Boolean get() = videoView.isParentView<VideoPipFloatView>()
    val isVideoPlaying: Boolean get() = videoView.isPlaying()
    val currentVideoTag: String? get() = tag

    private var tag: String? = null
    private var _videoListener: OnPipListener? = null
    private var _playlist = true

    private val videoListener get() = _videoListener
    private val floatView = VideoPipFloatView(VideoProvider.appContext)
    private val videoView = AppCompatVideoView(VideoProvider.appContext)
        .doOnPlayBuffered { it.refreshVideoSize(VideoSizeChangedType.BUFFERED) }
        .doOnVideoSize { view, _ -> view.refreshVideoSize(VideoSizeChangedType.VIDEO_SIZE_CALLBACK) }
        .doOnPlayCompleted { onPipOperateClickNext() }
        .doOnPlayError { videoListener?.onVideoPlayError() }

    fun registerListener(listener: OnPipListener) {
        _videoListener = listener
    }

    fun isPlayList(playlist: Boolean) = apply {
        this._playlist = playlist
    }

    fun preLoadVideo(root: ViewGroup?, forceViewGroup: Boolean, tag: String, name: String) {
        this.tag = tag
        videoView.release()
        if (!isPipFloatView || forceViewGroup) {
            if (root == null) throw NullPointerException("root == null")
            addView(root, name)
        } else {
            addWindow()
        }
    }

    fun startVideo(url: String) {
        videoView.startVideo(url)
    }

    fun startVideo(
        root: ViewGroup?,
        forceViewGroup: Boolean,
        tag: String,
        name: String,
        url: String
    ) {
        preLoadVideo(root, forceViewGroup, tag, name)
        startVideo(url)
    }

    fun addWindow() {
        floatView.addToWindow(
            videoView.getPipView(this, this, this)
        )
        videoView.refreshVideoSize(VideoSizeChangedType.ADD_WINDOW)
    }

    fun addView(viewGroup: ViewGroup, name: String) {
        viewGroup.visible()
        viewGroup.addView(
            videoView.getViewGroup(PlayerUtils.scanForActivity(viewGroup.context), name, this),
            matchViewGroupParams
        )
        videoView.refreshVideoSize(VideoSizeChangedType.ADD_VIEW_GROUP)
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
        _videoListener = null
        tag = null
    }

    fun onPause() {
        if (isPipFloatView) return
        videoView.onPause()
    }

    fun onResume() {
        if (isPipFloatView) return
        videoView.onResume()
    }

    fun onDestroy() {
        if (isPipFloatView) return
        shutDown()
    }

    fun onBackPressed(): Boolean {
        if (isPipFloatView) return false
        return videoView.onBackPressed()
    }

    override fun onPipOperatePlayList(): Boolean {
        return _playlist
    }

    /******************* listener method start *************************/
    override fun onPipOperateClickClose() {
        shutDown()
    }

    override fun onPipOperateClickRestore() {
        videoListener?.onPipRestore()
    }

    override fun onPipOperateClickNext() {
        videoListener?.onVideoPlayNext()
    }

    override fun onPipOperateClickPrev() {
        videoListener?.onVideoPlayPrev()
    }

    override fun onPipOperateClickRotation() {
        videoView.refreshRotation()
    }

    override fun onPipOperateClickScreenScale() {
        videoView.refreshScreenScaleType()
    }

    override fun onPipErrorPlayList(): Boolean {
        return _playlist
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
        return _playlist
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

    override fun onViewOrientationRotationClick() {
        onPipOperateClickRotation()
    }

    override fun onViewOrientationScreenScaleClick() {
        onPipOperateClickScreenScale()
    }

    /******************* listener method end *************************/

}