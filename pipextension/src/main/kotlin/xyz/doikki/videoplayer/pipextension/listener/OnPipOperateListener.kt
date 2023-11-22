package xyz.doikki.videoplayer.pipextension.listener

internal interface OnPipOperateListener {
    fun onPipOperatePlayList(): Boolean
    fun onPipOperateClickClose()
    fun onPipOperateClickRestore()
    fun onPipOperateClickNext()
    fun onPipOperateClickPrev()
    fun onPipOperateClickRotation()
    fun onPipOperateClickScreenScale()
}