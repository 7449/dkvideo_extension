package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import xyz.doikki.videoplayer.pipextension.VideoInitializer
import xyz.doikki.videoplayer.pipextension.simple.widget.SimpleVideoOverlayView

internal object OverlayHelper {

    private var overlayView: SimpleVideoOverlayView? = null

    fun get(): SimpleVideoOverlayView {
        if (overlayView == null) {
            overlayView = SimpleVideoOverlayView(VideoInitializer.appContext)
        }
        return requireNotNull(overlayView)
    }

    fun removeFromWindow() {
        overlayView?.removeFromWindow()
        overlayView = null
    }

    fun isOverlay(): Boolean {
        return overlayView?.isOverlay() == true
    }

    fun release() {
        removeFromWindow()
    }

}