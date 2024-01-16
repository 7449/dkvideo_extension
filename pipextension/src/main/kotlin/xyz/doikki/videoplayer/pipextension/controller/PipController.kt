package xyz.doikki.videoplayer.pipextension.controller

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.controller.GestureVideoController
import xyz.doikki.videoplayer.pipextension.listener.OnPipCompleteListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipErrorListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipOperateListener
import xyz.doikki.videoplayer.pipextension.view.pip.PipCompleteView
import xyz.doikki.videoplayer.pipextension.view.pip.PipErrorView
import xyz.doikki.videoplayer.pipextension.view.pip.PipOperateView
import xyz.doikki.videoplayer.pipextension.view.pip.PipProgressView

internal class PipController @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : GestureVideoController(context, attrs, defStyleAttr) {

    override fun getLayoutId(): Int {
        return 0
    }

    fun setDefaultControlComponent(
        operateListener: OnPipOperateListener,
        errorListener: OnPipErrorListener,
        completeListener: OnPipCompleteListener,
    ) {
        addControlComponent(PipProgressView(context))
        addControlComponent(PipCompleteView(context).registerListener(completeListener))
        addControlComponent(PipErrorView(context).registerListener(errorListener))
        addControlComponent(PipOperateView(context).registerListener(operateListener))
    }

}