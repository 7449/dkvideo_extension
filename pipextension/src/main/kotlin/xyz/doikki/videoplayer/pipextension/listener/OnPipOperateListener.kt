package xyz.doikki.videoplayer.pipextension.listener

interface OnPipOperateListener {
    fun onPipOperateClickClose()
    fun onPipOperateClickRestore()
    fun onPipOperateClickNext()
    fun onPipOperateClickPrev()
    fun onPipOperateClickRotation()
    fun onPipOperateClickScreenScale()
}