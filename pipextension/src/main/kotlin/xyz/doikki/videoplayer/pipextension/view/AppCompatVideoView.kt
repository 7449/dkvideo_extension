package xyz.doikki.videoplayer.pipextension.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayContainerBinding
import xyz.doikki.videoplayer.pipextension.listener.OnPipCompleteListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipErrorListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipOperateListener
import xyz.doikki.videoplayer.pipextension.listener.OnViewOperateListener
import xyz.doikki.videoplayer.pipextension.types.VideoSizeChangedType
import xyz.doikki.videoplayer.pipextension.view.helper.VideoViewApiHelper
import xyz.doikki.videoplayer.pipextension.view.helper.VideoViewOrientationHelper
import xyz.doikki.videoplayer.pipextension.view.helper.VideoViewStateHelper
import xyz.doikki.videoplayer.pipextension.view.helper.VideoViewUiHelper

internal class AppCompatVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding =
        VideoLayoutPlayContainerBinding.inflate(LayoutInflater.from(context), this, true)

    private val apiHelper = VideoViewApiHelper(viewBinding.progress, viewBinding.videoView)
    private val orientationHelper = VideoViewOrientationHelper(this, viewBinding.videoView)
    private val stateHelper = VideoViewStateHelper(this, viewBinding.videoView)
    private val uiHelper = VideoViewUiHelper(this, viewBinding.root, viewBinding.videoView)

    fun doOnPlayCompleted(action: (view: AppCompatVideoView) -> Unit) = apply {
        stateHelper.doOnPlayCompleted(action)
    }

    fun doOnPlayBuffering(action: (view: AppCompatVideoView) -> Unit) = apply {
        stateHelper.doOnPlayBuffering(action)
    }

    fun doOnPlayBuffered(action: (view: AppCompatVideoView) -> Unit) = apply {
        stateHelper.doOnPlayBuffered(action)
    }

    fun doOnPlayError(action: (view: AppCompatVideoView) -> Unit) = apply {
        stateHelper.doOnPlayError(action)
    }

    fun doOnPlaying(action: (view: AppCompatVideoView) -> Unit) = apply {
        stateHelper.doOnPlaying(action)
    }

    fun doOnVideoSize(action: (view: AppCompatVideoView, size: IntArray) -> Unit) = apply {
        stateHelper.doOnVideoSize(action)
    }

    fun getPipVideo(
        operateListener: OnPipOperateListener,
        errorListener: OnPipErrorListener,
        completeListener: OnPipCompleteListener,
    ) = apply {
        orientationHelper.releaseRotation()
        orientationHelper.releaseScreenScaleType()
        uiHelper.pipControllerView(operateListener, errorListener, completeListener)
    }

    fun getViewVideo(
        activity: Activity,
        title: String,
        operateListener: OnViewOperateListener,
    ) = apply {
        orientationHelper.releaseRotation()
        orientationHelper.releaseScreenScaleType()
        uiHelper.viewGroupControllerView(activity, title, operateListener)
    }

    fun startVideo(url: String) {
        apiHelper.startVideo(url)
    }

    fun isPlaying(): Boolean {
        return apiHelper.isPlaying()
    }

    fun release() {
        apiHelper.release()
        orientationHelper.releaseRotation()
        orientationHelper.releaseScreenScaleType()
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

    fun showProgressView() {
        apiHelper.showProgressView()
    }

    fun showVideoView() {
        apiHelper.showVideoView()
    }

    fun refreshRotation() {
        orientationHelper.refreshRotation()
    }

    fun refreshScreenScaleType() {
        orientationHelper.refreshScreenScaleType()
    }

    fun refreshVideoSize(videoSizeChangedType: VideoSizeChangedType) {
        orientationHelper.refreshVideoSize(videoSizeChangedType)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        orientationHelper.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        orientationHelper.onDetachedFromWindow()
    }

}