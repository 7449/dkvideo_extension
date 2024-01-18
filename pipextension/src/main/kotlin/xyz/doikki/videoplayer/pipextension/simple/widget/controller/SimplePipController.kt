package xyz.doikki.videoplayer.pipextension.simple.widget.controller

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.controller.GestureVideoController
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipCompleteComponent
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipComponent
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipErrorComponent
import xyz.doikki.videoplayer.pipextension.simple.widget.component.SimplePipProgressComponent

class SimplePipController @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : GestureVideoController(context, attrs, defStyleAttr) {

    override fun getLayoutId(): Int {
        return 0
    }

    fun addControlComponents() {
        addControlComponent(SimplePipProgressComponent(context))
        addControlComponent(SimplePipCompleteComponent(context))
        addControlComponent(SimplePipErrorComponent(context))
        addControlComponent(SimplePipComponent(context))
    }

}