package xyz.doikki.videoplayer.pipextension.listener

internal interface OnPipCompleteListener {
    fun onPipCompletePlayList(): Boolean
    fun onPipCompleteClickClose()
    fun onPipCompleteClickRestore()
    fun onPipCompleteClickNext()
    fun onPipCompleteClickPrev()
}