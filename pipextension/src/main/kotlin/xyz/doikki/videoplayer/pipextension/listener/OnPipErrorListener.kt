package xyz.doikki.videoplayer.pipextension.listener

internal interface OnPipErrorListener {
    fun onPipErrorPlayList(): Boolean
    fun onPipErrorClickClose()
    fun onPipErrorClickRestore()
    fun onPipErrorClickNext()
    fun onPipErrorClickPrev()
}