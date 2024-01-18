package xyz.doikki.videoplayer.pipextension.simple

import xyz.doikki.videoplayer.player.BaseVideoView

enum class SimpleVideoState(val state: Int) {
    ERROR(BaseVideoView.STATE_ERROR),
    IDLE(BaseVideoView.STATE_IDLE),
    PREPARING(BaseVideoView.STATE_PREPARING),
    PREPARED(BaseVideoView.STATE_PREPARED),
    PLAYING(BaseVideoView.STATE_PLAYING),
    PAUSED(BaseVideoView.STATE_PAUSED),
    PLAYBACK_COMPLETED(BaseVideoView.STATE_PLAYBACK_COMPLETED),
    BUFFERING(BaseVideoView.STATE_BUFFERING),
    BUFFERED(BaseVideoView.STATE_BUFFERED),
    START_ABORT(BaseVideoView.STATE_START_ABORT),
}