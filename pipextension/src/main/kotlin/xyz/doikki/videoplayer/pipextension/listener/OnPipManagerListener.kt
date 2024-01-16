package xyz.doikki.videoplayer.pipextension.listener

interface OnPipManagerListener {
    fun onPipEntry()
    fun onPipRestore()
    fun onPipPlayPrev()
    fun onPipPlayNext()
    fun onPipPlayError()
}