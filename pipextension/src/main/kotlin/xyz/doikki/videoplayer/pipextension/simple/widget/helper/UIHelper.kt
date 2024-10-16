package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import xyz.doikki.videoplayer.controller.OrientationHelper
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoOverlayView
import xyz.doikki.videoplayer.pipextension.simple.develop.dp
import xyz.doikki.videoplayer.pipextension.simple.develop.inspectSize
import xyz.doikki.videoplayer.pipextension.simple.develop.isLandscape
import xyz.doikki.videoplayer.pipextension.simple.develop.isMp3Url
import xyz.doikki.videoplayer.pipextension.simple.develop.isOverlayParent
import xyz.doikki.videoplayer.pipextension.simple.develop.parentView
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoView
import xyz.doikki.videoplayer.player.BaseVideoView

internal class UIHelper(private val videoView: SimpleVideoView) :
    OrientationHelper.OnOrientationChangeListener {

    companion object {
        private const val DEFAULT_VIDEO_MARGIN = 100
    }

    private enum class ScreenOrientation { LANDSCAPE, PORT, }

    private val orientationHelper = OrientationHelper(videoView.context)

    private val screenScaleHelper = ScreenScaleHelper(videoView)
    private val rotationHelper = RotationHelper(videoView)

    private var currentOrientationType = orientationType()

    override fun onOrientationChanged(orientation: Int) {
        val resources = videoView.resources
        val widthPixels = resources.displayMetrics.widthPixels
        val heightPixels = resources.displayMetrics.heightPixels
        val value = if (widthPixels > heightPixels) ScreenOrientation.LANDSCAPE
        else ScreenOrientation.PORT
        if (currentOrientationType != value) {
            refreshVideoSize()
            currentOrientationType = value
        }
    }

    fun onAttachedToWindow() {
        currentOrientationType = orientationType()
        orientationHelper.setOnOrientationChangeListener(this)
        orientationHelper.enable()
    }

    fun onDetachedFromWindow() {
        orientationHelper.setOnOrientationChangeListener(null)
        orientationHelper.disable()
    }

    fun refreshRotation() {
        rotationHelper.refresh()
        refreshVideoSize()
    }

    fun releaseRotation() {
        rotationHelper.release()
    }

    fun refreshScreenScale() {
        screenScaleHelper.refresh()
    }

    fun releaseScreenScale() {
        screenScaleHelper.release()
    }

    private fun orientationType(): ScreenOrientation {
        return if (videoView.context.isLandscape) ScreenOrientation.LANDSCAPE else ScreenOrientation.PORT
    }

    fun refreshVideoSize() {
        if (videoView.url.isBlank()) return
        if (!videoView.isMp3Url() && !videoView.inspectSize()) return

        val newVideoSize = if (videoView.isOverlayParent())
            correctPipVideoSize()
        else
            correctViewVideoSize()

        videoView.parentView<SimpleVideoOverlayView>()
            ?.updateViewLayout(newVideoSize)
            ?: videoView.parentView<ViewGroup>()?.updateLayoutParams {
                width = newVideoSize.first()
                height = newVideoSize.last()
                if (this is FrameLayout.LayoutParams) {
                    gravity = Gravity.CENTER
                }
            }

        screenScaleHelper.release()

        Log.e(
            "Print",
            "RefreshVideoSize:"
                    + "\n视频原尺寸:${videoView.videoSize.contentToString()}"
                    + "\n视频新尺寸:${newVideoSize.contentToString()}"
        )
    }

    private fun correctPipVideoSize(): IntArray {
        val defaultSize = rotationHelper.rotationVideoSize()
        val width = defaultSize.first()
        val height = defaultSize.last()

        val metrics = videoView.context.resources.displayMetrics
        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels

        val landScapeWidth = widthPixels.coerceAtMost(heightPixels) - DEFAULT_VIDEO_MARGIN
        if (videoView.isMp3Url()) return intArrayOf(landScapeWidth, 150f.dp)

        val landScapeHeight = (landScapeWidth.toFloat() / width * height).toInt()
        if (width >= height) return intArrayOf(landScapeWidth, landScapeHeight)
        val portWidth = landScapeWidth / (if (widthPixels < heightPixels) 1.7 else 2.0)
        val portHeight = portWidth / width * height
        return intArrayOf(portWidth.toInt(), portHeight.toInt())
    }

    private fun correctViewVideoSize(): IntArray {
        val defaultSize = rotationHelper.rotationVideoSize()
        val width = defaultSize.first()
        val height = defaultSize.last()

        val metrics = videoView.resources.displayMetrics
        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels

        if (videoView.isMp3Url()) return intArrayOf(widthPixels, 150f.dp)
        if (widthPixels < heightPixels || !videoView.context.isLandscape) {
            if (width >= height)
                return intArrayOf(widthPixels, (widthPixels.toFloat() / width * height).toInt())
            return intArrayOf(widthPixels, heightPixels / 2)
        } else if (videoView.isFullScreen) {
            return intArrayOf(-1, -1)
        }

        return intArrayOf(widthPixels, (heightPixels / 1.5).toInt())
    }

    private class RotationHelper(private val videoView: SimpleVideoView) {

        private enum class Rotation(val rotation: Int) {
            R_0(0), R_90(90), R_180(180), R_270(270),
        }

        private val rotationArray = Rotation.entries
        private var rotationIndex = 0

        fun rotationVideoSize(): IntArray {
            val videoSize = videoView.videoSize
            val currentRotation = rotationArray[rotationIndex]
            return when (currentRotation) {
                Rotation.R_90, Rotation.R_270 -> videoSize.reversed().toIntArray()
                else -> videoSize
            }
        }

        fun release() {
            rotationIndex = 0
            applyRotation()
        }

        fun refresh() {
            rotationIndex = (rotationIndex + 1) % rotationArray.size
            applyRotation()
        }

        private fun applyRotation() {
            videoView.rotation = rotationArray[rotationIndex].rotation.toFloat()
        }

    }

    private class ScreenScaleHelper(private val videoView: SimpleVideoView) {

        private enum class ScreenScale(val scale: Int) {
            SCREEN_SCALE_DEFAULT(BaseVideoView.SCREEN_SCALE_DEFAULT),
            SCREEN_SCALE_16_9(BaseVideoView.SCREEN_SCALE_16_9),
            SCREEN_SCALE_4_3(BaseVideoView.SCREEN_SCALE_4_3),
            SCREEN_SCALE_MATCH_PARENT(BaseVideoView.SCREEN_SCALE_MATCH_PARENT),
            SCREEN_SCALE_ORIGINAL(BaseVideoView.SCREEN_SCALE_ORIGINAL),
            SCREEN_SCALE_CENTER_CROP(BaseVideoView.SCREEN_SCALE_CENTER_CROP),
        }

        private val scaleTypes = ScreenScale.entries
        private var scaleIndex = 0

        fun release() {
            scaleIndex = 0
            applyScreenScaleType()
        }

        fun refresh() {
            scaleIndex = (scaleIndex + 1) % scaleTypes.size
            applyScreenScaleType()
        }

        private fun applyScreenScaleType() {
            val scaleType = scaleTypes[scaleIndex]
            videoView.setScreenScaleType(scaleType.scale)
        }

    }

}