package xyz.doikki.videoplayer.pipextension.simple.develop

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.GestureVideoController
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayPreloadBinding

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

    class SimpleVideoPreloadComponent(context: Context) : SimpleVideoComponent(context) {

        init {
            addView(
                VideoLayoutPlayPreloadBinding
                    .inflate(LayoutInflater.from(context), this, false).root
            )
            isVisible = false
        }

        override fun setVisibility(visibility: Int) {
            super.setVisibility(visibility)
            if (visibility == VISIBLE) {
                bringToFront()
            }
        }

    }

}