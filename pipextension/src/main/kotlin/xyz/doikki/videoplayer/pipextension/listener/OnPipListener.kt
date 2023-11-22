package xyz.doikki.videoplayer.pipextension.listener

interface OnPipListener {
    fun onPipRestore()
    fun onVideoPlayPrev()
    fun onVideoPlayNext()
    fun onVideoPlayError()
}