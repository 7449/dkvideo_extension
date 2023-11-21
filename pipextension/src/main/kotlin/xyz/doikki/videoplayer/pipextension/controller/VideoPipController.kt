package xyz.doikki.videoplayer.pipextension.controller

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.controller.GestureVideoController
import xyz.doikki.videoplayer.pipextension.listener.OnPipCompleteListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipErrorListener
import xyz.doikki.videoplayer.pipextension.listener.OnPipOperateListener
import xyz.doikki.videoplayer.pipextension.view.pip.VideoPipCompleteView
import xyz.doikki.videoplayer.pipextension.view.pip.VideoPipErrorView
import xyz.doikki.videoplayer.pipextension.view.pip.VideoPipOperateView
import xyz.doikki.videoplayer.pipextension.view.pip.VideoPipProgressView

internal class VideoPipController @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : GestureVideoController(context, attrs, defStyleAttr) {

    override fun getLayoutId(): Int {
        return 0
    }

    fun setDefaultControlComponent(
        operateListener: OnPipOperateListener,
        errorListener: OnPipErrorListener,
        completeListener: OnPipCompleteListener,
    ) {
        addControlComponent(VideoPipProgressView(context))
        addControlComponent(VideoPipCompleteView(context).registerListener(completeListener))
        addControlComponent(VideoPipErrorView(context).registerListener(errorListener))
        addControlComponent(VideoPipOperateView(context).registerListener(operateListener))
    }

}