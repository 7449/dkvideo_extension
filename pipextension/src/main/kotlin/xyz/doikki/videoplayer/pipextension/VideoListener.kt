package xyz.doikki.videoplayer.pipextension

internal interface VideoListener {
    fun onEntryPip()
    fun onEntryActivity()
    fun onVideoPlayPrev()
    fun onVideoPlayNext()
    fun onVideoPlayError()
}