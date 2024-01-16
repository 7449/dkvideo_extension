package xyz.doikki.videoplayer.pipextension.types

internal enum class VideoSizeChangedType {
    REFRESH_ROTATION,
    ORIENTATION,
    ATTACH_WINDOW,
    ATTACH_VIEW_GROUP,
    VIDEO_SIZE_CALLBACK,
    NEW_VIDEO,
    BUFFERED,
    ;
}