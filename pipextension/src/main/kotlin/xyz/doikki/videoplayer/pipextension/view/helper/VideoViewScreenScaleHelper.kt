package xyz.doikki.videoplayer.pipextension.view.helper

import xyz.doikki.videoplayer.pipextension.types.VideoScreenScaleType
import xyz.doikki.videoplayer.pipextension.view.view.VideoView

internal class VideoViewScreenScaleHelper(private val videoView: VideoView) {

    private val screenScaleTypes = VideoScreenScaleType.entries
    private var screenScaleType = VideoScreenScaleType.SCREEN_SCALE_DEFAULT
    private var screenScaleIndex = 1

    fun releaseScreenScaleType() {
        screenScaleType = VideoScreenScaleType.SCREEN_SCALE_DEFAULT
        screenScaleIndex = 1
        onChangedVideoScreenScaleType(screenScaleType)
    }

    fun refreshScreenScaleType() {
        onChangedVideoScreenScaleType(screenScaleTypes[screenScaleIndex].apply {
            screenScaleIndex = (screenScaleIndex + 1) % screenScaleTypes.size
        })
    }

    private fun onChangedVideoScreenScaleType(scaleType: VideoScreenScaleType) {
        videoView.setScreenScaleType(scaleType.scale)
        screenScaleType = scaleType
    }

}