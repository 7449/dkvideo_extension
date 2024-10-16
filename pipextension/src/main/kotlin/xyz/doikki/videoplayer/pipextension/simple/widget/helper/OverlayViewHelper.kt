package xyz.doikki.videoplayer.pipextension.simple.widget.helper

import android.content.Context
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoOverlayView

internal object OverlayViewHelper {

    private var overlayView: SimpleVideoOverlayView? = null

    fun get(context: Context): SimpleVideoOverlayView {
        if (overlayView == null) {
            overlayView = SimpleVideoOverlayView(context)
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