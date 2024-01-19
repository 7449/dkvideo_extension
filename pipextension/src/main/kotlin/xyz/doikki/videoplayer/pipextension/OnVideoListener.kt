package xyz.doikki.videoplayer.pipextension

import android.view.ViewGroup

interface OnVideoListener {
    fun onSwitchPipMode(container: ViewGroup?)
    fun onPipComeBackActivity()
    fun onVideoPlayPrev()
    fun onVideoPlayNext()
    fun onVideoPlayError()
}