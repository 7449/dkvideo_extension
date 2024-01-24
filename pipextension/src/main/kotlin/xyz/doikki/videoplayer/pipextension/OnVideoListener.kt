package xyz.doikki.videoplayer.pipextension

import android.view.ViewGroup

interface OnVideoListener {
    fun onSwitchPipMode(parent: ViewGroup?)
    fun onPipComeBackActivity(parent: ViewGroup?)
    fun onVideoPlayPrev(parent: ViewGroup?)
    fun onVideoPlayNext(parent: ViewGroup?)
    fun onVideoPlayError(parent: ViewGroup?)
}