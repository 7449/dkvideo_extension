package xyz.doikki.videoplayer.pipextension.simple

import android.os.Bundle
import xyz.doikki.videoplayer.pipextension.fullScreen

abstract class SimpleVideoListActivity(layout: Int = 0) : SimpleVideoActivity(layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
    }

}