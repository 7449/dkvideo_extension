package xyz.doikki.videoplayer.pipextension.view.helper

import xyz.doikki.videoplayer.pipextension.types.VideoRotation
import xyz.doikki.videoplayer.pipextension.view.view.VideoView

internal class VideoViewRotationHelper(private val videoView: VideoView) {

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