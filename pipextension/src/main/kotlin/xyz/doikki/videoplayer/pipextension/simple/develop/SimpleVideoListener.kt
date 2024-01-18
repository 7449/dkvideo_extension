package xyz.doikki.videoplayer.pipextension.simple.develop

import xyz.doikki.videoplayer.pipextension.OnVideoListener

class SimpleVideoListener(
    private val pipEntry: () -> Unit = {},
    private val pipRestore: () -> Unit = {},
    private val pipPlayPrev: () -> Unit = {},
    private val pipPlayNext: () -> Unit = {},
    private val pipPlayError: () -> Unit = {},
) : OnVideoListener {
    override fun onEntryPipMode() {
        pipEntry()
    }

    override fun onPipComeBackActivity() {
        pipRestore()
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