package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import xyz.doikki.videoplayer.pipextension.initializer.VideoInitializer
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoOverlayView

object OverlayHelper {

    private var overlayView: SimpleVideoOverlayView? = null

    fun get(): SimpleVideoOverlayView {
        if (overlayView == null) {
            overlayView = SimpleVideoOverlayView(VideoInitializer.appContext)
        }
        return requireNotNull(overlayView)
    }

    fun removeFromWindow() {
        overlayView?.removeFromWindow()
    }

    fun isOverlay(): Boolean {
        return overlayView?.isOverlay() == true
    }

    fun release() {
        removeFromWindow()
        overlayView = null
    }

}