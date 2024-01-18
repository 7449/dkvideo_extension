package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import android.util.Log
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import xyz.doikki.videoplayer.controller.OrientationHelper
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.VideoOrientation
import xyz.doikki.videoplayer.pipextension.VideoRotation
import xyz.doikki.videoplayer.pipextension.VideoScreenScale
import xyz.doikki.videoplayer.pipextension.VideoSizeChanged
import xyz.doikki.videoplayer.pipextension.dp2px
import xyz.doikki.videoplayer.pipextension.isLandscape
import xyz.doikki.videoplayer.pipextension.parentView
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoOverlayView
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoContainerView

class OrienHelper(
    private val appCompatVideoView: SimpleVideoContainerView,
    private val videoView: SimpleVideoView,
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
        val value = if (widthPixels > heightPixels) VideoOrientation.LANDSCAPE
        else VideoOrientation.PORT
        if (screenOrientation != value) {
            refreshVideoSize(VideoSizeChanged.ORIENTATION)
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
        refreshVideoSize(VideoSizeChanged.REFRESH_ROTATION)
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

    fun refreshVideoSize(type: VideoSizeChanged) {
        if (videoView.url.isBlank()) return
        if (!videoView.isMp3 && !videoView.inspectSize) return

        val newVideoSize = if (appCompatVideoView.parentView<SimpleVideoOverlayView>() != null)
            pipVideoSize() else viewVideoSize()

        appCompatVideoView.parentView<SimpleVideoOverlayView>()
            ?.updateViewLayout(newVideoSize)
            ?: appCompatVideoView.parentView<ViewGroup>()?.updateLayoutParams {
                width = newVideoSize.first()
                height = newVideoSize.last()
            }

        screenScaleHelper.releaseScreenScaleType()

        Log.e(
            "Print",
            "RefreshVideoSize: \n来源:$type ${VideoManager.instance.videoTag} "
                    + "\n视频原尺寸:${videoView.videoSize.contentToString()}"
                    + "\n视频新尺寸:${newVideoSize.contentToString()}"
        )

    }

    private fun refreshOrientationType(): VideoOrientation {
        return if (videoView.context.isLandscape) VideoOrientation.LANDSCAPE else VideoOrientation.PORT
    }

    private fun pipVideoSize(): IntArray {
        val defaultSize = rotationHelper.videoSize()
        val width = defaultSize.first()
        val height = defaultSize.last()

        val metrics = videoView.context.resources.displayMetrics
        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels

        val landScapeWidth = widthPixels.coerceAtMost(heightPixels) - DEFAULT_VIDEO_MARGIN
        if (videoView.isMp3) return intArrayOf(landScapeWidth, 150f.dp2px())
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

        if (videoView.isMp3) return intArrayOf(widthPixels, 150f.dp2px())

        if (widthPixels < heightPixels || !videoView.context.isLandscape) {
            if (width >= height)
                return intArrayOf(widthPixels, (widthPixels.toFloat() / width * height).toInt())
            return intArrayOf(widthPixels, heightPixels / 2)
        } else if (videoView.isFullScreen) {
            return intArrayOf(-1, -1)
        }

        return intArrayOf(widthPixels, (heightPixels / 1.5).toInt())
    }

    private class VideoViewRotationHelper(private val videoView: SimpleVideoView) {

        private val videoRotationArray = VideoRotation.entries
        private var videoRotation = VideoRotation.R_360
        private var videoRotationIndex = 0

        fun releaseRotation() {
            videoRotation = VideoRotation.R_360
            videoRotationIndex = 0
            videoView.rotation = 0f
        }

        fun refreshVideoRotation() {
            onChangedVideoRotation(videoRotationArray[videoRotationIndex].apply {
                videoRotationIndex = (videoRotationIndex + 1) % videoRotationArray.size
            })
        }

        fun videoSize(): IntArray {
            val videoSize = videoView.videoSize
            return when (videoRotation) {
                VideoRotation.R_90 -> intArrayOf(videoSize.last(), videoSize.first())
                VideoRotation.R_270 -> intArrayOf(videoSize.last(), videoSize.first())
                else -> videoSize
            }
        }

        private fun onChangedVideoRotation(rotation: VideoRotation) {
            videoView.rotation = rotation.rotation.toFloat()
            videoRotation = rotation
        }

    }

    private class VideoViewScreenScaleHelper(private val videoView: SimpleVideoView) {

        private val screenScaleTypes = VideoScreenScale.entries
        private var screenScaleType = VideoScreenScale.SCREEN_SCALE_DEFAULT
        private var screenScaleIndex = 1

        fun releaseScreenScaleType() {
            screenScaleType = VideoScreenScale.SCREEN_SCALE_DEFAULT
            screenScaleIndex = 1
            onChangedVideoScreenScaleType(screenScaleType)
        }

        fun refreshScreenScaleType() {
            onChangedVideoScreenScaleType(screenScaleTypes[screenScaleIndex].apply {
                screenScaleIndex = (screenScaleIndex + 1) % screenScaleTypes.size
            })
        }

        private fun onChangedVideoScreenScaleType(scaleType: VideoScreenScale) {
            videoView.setScreenScaleType(scaleType.scale)
            screenScaleType = scaleType
        }

    }

}