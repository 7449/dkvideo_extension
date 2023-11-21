package xyz.doikki.videoplayer.pipextension.listener

interface OnSingleVideoListener {
    fun onPipRestore()
    fun onVideoPlayPrev()
    fun onVideoPlayNext()
    fun onVideoPlayError()
}