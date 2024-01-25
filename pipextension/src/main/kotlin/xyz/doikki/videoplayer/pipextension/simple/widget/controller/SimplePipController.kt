package xyz.doikki.videoplayer.pipextension.simple.widget.controller

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.controller.GestureVideoController
import xyz.doikki.videoplayer.pipextension.gone
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimpleAnimComponent
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipCompleteComponent
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipComponent
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipErrorComponent
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipProgressComponent
import xyz.doikki.videoplayer.pipextension.visible

class SimplePipController @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : GestureVideoController(context, attrs, defStyleAttr) {

    private val animComponent = SimpleAnimComponent(context)

    override fun getLayoutId(): Int {
        return 0
    }

    fun addControlComponents() {
        addControlComponent(SimplePipProgressComponent(context))
        addControlComponent(SimplePipCompleteComponent(context))
        addControlComponent(SimplePipErrorComponent(context))
        addControlComponent(SimplePipComponent(context))
        addControlComponent(animComponent)
    }

    fun showAnim() {
        animComponent.visible()
    }

    fun hideAnim() {
        animComponent.gone()
    }

}