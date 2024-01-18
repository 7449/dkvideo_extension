package xyz.doikki.videoplayer.pipextension

interface OnVideoListener {
    fun onEntryPipMode()
    fun onPipComeBackActivity()
    fun onVideoPlayPrev()
    fun onVideoPlayNext()
    fun onVideoPlayError()
}