package xyz.doikki.videoplayer.pipextension.simple.develop

import android.view.ViewGroup
import xyz.doikki.videoplayer.pipextension.OnVideoListener

class SimpleVideoListener(
    private val switchPipMode: (ViewGroup?) -> Unit = {},
    private val pipComeBackActivity: (ViewGroup?) -> Unit = {},
    private val pipPlayPrev: (ViewGroup?) -> Unit = {},
    private val pipPlayNext: (ViewGroup?) -> Unit = {},
    private val pipPlayError: (ViewGroup?) -> Unit = {},
) : OnVideoListener {
    override fun onSwitchPipMode(parent: ViewGroup?) {
        switchPipMode(parent)
    }

    override fun onPipComeBackActivity(parent: ViewGroup?) {
        pipComeBackActivity(parent)
    }

    override fun onVideoPlayPrev(parent: ViewGroup?) {
        pipPlayPrev(parent)
    }

    override fun onVideoPlayNext(parent: ViewGroup?) {
        pipPlayNext(parent)
    }

    override fun onVideoPlayError(parent: ViewGroup?) {
        pipPlayError(parent)
    }
}