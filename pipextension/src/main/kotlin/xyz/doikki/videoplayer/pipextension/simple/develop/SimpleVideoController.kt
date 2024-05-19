package xyz.doikki.videoplayer.pipextension.simple.develop

import android.content.Context
import androidx.core.view.isVisible
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.GestureVideoController
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimpleVideoPreloadComponent

internal abstract class SimpleVideoController(
    context: Context
) : GestureVideoController(context, null, 0), SimpleVideoPreload {

    override fun getLayoutId(): Int = 0

    protected val preloadComponent = SimpleVideoPreloadComponent(context)
    protected val controlWrapper: ControlWrapper? get() = mControlWrapper

    override fun showVideoPreloadAnim() {
        preloadComponent.isVisible = true
    }

    override fun hideVideoPreloadAnim() {
        preloadComponent.isVisible = false
    }

}