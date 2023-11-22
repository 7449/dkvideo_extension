package xyz.doikki.videoplayer.pipextension.view.helper

import android.util.Log
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import xyz.doikki.videoplayer.controller.OrientationHelper
import xyz.doikki.videoplayer.pipextension.PipVideoManager
import xyz.doikki.videoplayer.pipextension.dp2pxInt
import xyz.doikki.videoplayer.pipextension.isLandscape
import xyz.doikki.videoplayer.pipextension.types.VideoScreenOrientation
import xyz.doikki.videoplayer.pipextension.types.VideoSizeChangedType
import xyz.doikki.videoplayer.pipextension.view.AppCompatVideoView
import xyz.doikki.videoplayer.pipextension.view.VideoPipFloatView
import xyz.doikki.videoplayer.pipextension.view.parentView
import xyz.doikki.videoplayer.pipextension.view.view.VideoView

internal class VideoViewOrientationHelper(
    private val appCompatVideoView: AppCompatVideoView,
    private val videoView: VideoView
) : OrientationHelper.OnOrientationChangeListener {

    companion object {
        private const val DEFAULT_VIDEO_MARGIN = 100
    }

    private val screenScaleHelper = VideoViewScreenScaleHelper(videoView)
    private val rotationHelper = VideoViewRotationHelper(videoView)
    private val orientationHelper = OrientationHelper(videoView.context)
    private var screenOrientation = refreshOrientationType()

    override fun onOrientationChanged(orientation: Int) {
        val resources = videoView.resources
        val widthPixels = resources.displayMetrics.widthPixels
        val heightPixels = resources.displayMetrics.heightPixels
        val value = if (widthPixels > heightPixels) VideoScreenOrientation.LANDSCAPE
        else VideoScreenOrientation.PORT
        if (screenOrientation != value) {
            refreshVideoSize(VideoSizeChangedType.ORIENTATION)
            screenOrientation = value
        }
    }

    fun onAttachedToWindow() {
        screenOrientation = refreshOrientationType()
        orientationHelper.setOnOrientationChangeListener(this)
        orientationHelper.enable()
    }

    fun onDetachedFromWindow() {
        orientationHelper.setOnOrientationChangeListener(null)
        orientationHelper.disable()
    }

    fun refreshRotation() {
        rotationHelper.refreshVideoRotation()
        refreshVideoSize(VideoSizeChangedType.REFRESH_ROTATION)
    }

    fun releaseRotation() {
        rotationHelper.releaseRotation()
    }

    fun releaseScreenScaleType() {
        screenScaleHelper.releaseScreenScaleType()
    }

    fun refreshScreenScaleType() {
        screenScaleHelper.refreshScreenScaleType()
    }

    fun refreshVideoSize(type: VideoSizeChangedType) {
        if (videoView.url().isNullOrBlank()) return
        if (!videoView.isMp3() && !videoView.checkVideoSize()) return

        val newVideoSize =
            if (appCompatVideoView.parentView<VideoPipFloatView>() != null) pipVideoSize()
            else viewVideoSize()

        appCompatVideoView.parentView<VideoPipFloatView>()
            ?.updateViewLayout(newVideoSize)
            ?: appCompatVideoView.parentView<ViewGroup>()?.updateLayoutParams {
                width = newVideoSize.first()
                height = newVideoSize.last()
            }

        screenScaleHelper.releaseScreenScaleType()

        Log.e(
            "Print",
            "RefreshVideoSize: \n来源:$type ${PipVideoManager.instance.currentVideoTag} "
                    + "\n视频原尺寸:${videoView.videoSize.contentToString()}"
                    + "\n视频新尺寸:${newVideoSize.contentToString()}"
        )

    }

    private fun refreshOrientationType(): VideoScreenOrientation {
        return if (videoView.context.isLandscape) VideoScreenOrientation.LANDSCAPE else VideoScreenOrientation.PORT
    }

    private fun pipVideoSize(): IntArray {
        val defaultSize = rotationHelper.videoSize()
        val width = defaultSize.first()
        val height = defaultSize.last()

        val metrics = videoView.context.resources.displayMetrics
        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels

        val landScapeWidth = widthPixels.coerceAtMost(heightPixels) - DEFAULT_VIDEO_MARGIN
        if (videoView.isMp3()) return intArrayOf(landScapeWidth, 150f.dp2pxInt())
        val landScapeHeight = (landScapeWidth.toFloat() / width * height).toInt()
        if (width >= height) return intArrayOf(landScapeWidth, landScapeHeight)
        val portWidth = landScapeWidth / (if (widthPixels < heightPixels) 1.7 else 2.0)
        val portHeight = portWidth / width * height
        return intArrayOf(portWidth.toInt(), portHeight.toInt())
    }

    private fun viewVideoSize(): IntArray {
        val defaultSize = rotationHelper.videoSize()
        val width = defaultSize.first()
        val height = defaultSize.last()

        val metrics = videoView.context.resources.displayMetrics
        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels

        if (videoView.isMp3()) return intArrayOf(widthPixels, 150f.dp2pxInt())

        if (widthPixels < heightPixels || !videoView.context.isLandscape) {
            if (width >= height)
                return intArrayOf(widthPixels, (widthPixels.toFloat() / width * height).toInt())
            return intArrayOf(widthPixels, heightPixels / 2)
        } else if (videoView.isFullScreen) {
            return intArrayOf(-1, -1)
        }

        return intArrayOf(widthPixels, (heightPixels / 1.5).toInt())
    }

}