package xyz.doikki.videoplayer.pipextension.simple.widget.controller

import android.content.Context
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoController
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipComponent

internal class SimplePipController(context: Context) : SimpleVideoController(context) {

    fun addControlComponents() {
        addControlComponent(SimplePipComponent(context))
        addControlComponent(preloadComponent)
    }

}