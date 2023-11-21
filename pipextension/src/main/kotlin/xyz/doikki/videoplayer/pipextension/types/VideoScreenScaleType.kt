package xyz.doikki.videoplayer.pipextension.types

import xyz.doikki.videoplayer.player.BaseVideoView

enum class VideoScreenScaleType(val scale: Int) {

    SCREEN_SCALE_DEFAULT(BaseVideoView.SCREEN_SCALE_DEFAULT),
    SCREEN_SCALE_16_9(BaseVideoView.SCREEN_SCALE_16_9),
    SCREEN_SCALE_4_3(BaseVideoView.SCREEN_SCALE_4_3),
    SCREEN_SCALE_MATCH_PARENT(BaseVideoView.SCREEN_SCALE_MATCH_PARENT),
    SCREEN_SCALE_ORIGINAL(BaseVideoView.SCREEN_SCALE_ORIGINAL),
    SCREEN_SCALE_CENTER_CROP(BaseVideoView.SCREEN_SCALE_CENTER_CROP),

}