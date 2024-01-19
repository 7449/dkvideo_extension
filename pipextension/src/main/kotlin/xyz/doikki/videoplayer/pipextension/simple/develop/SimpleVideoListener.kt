package xyz.doikki.videoplayer.pipextension.simple.develop

import android.view.ViewGroup
import xyz.doikki.videoplayer.pipextension.OnVideoListener

class SimpleVideoListener(
    private val switchPipMode: (ViewGroup?) -> Unit = {},
    private val pipComeBackActivity: () -> Unit = {},
    private val pipPlayPrev: () -> Unit = {},
    private val pipPlayNext: () -> Unit = {},
    private val pipPlayError: () -> Unit = {},
) : OnVideoListener {
    override fun onSwitchPipMode(container: ViewGroup?) {
        switchPipMode(container)
    }

    override fun onPipComeBackActivity() {
        pipComeBackActivity()
    }

    override fun onVideoPlayPrev() {
        pipPlayPrev()
    }

    override fun onVideoPlayNext() {
        pipPlayNext()
    }

    override fun onVideoPlayError() {
        pipPlayError()
    }
}