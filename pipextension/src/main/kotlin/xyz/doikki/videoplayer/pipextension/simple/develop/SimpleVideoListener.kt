package xyz.doikki.videoplayer.pipextension.simple

import xyz.doikki.videoplayer.pipextension.OnVideoListener

class SimpleVideoListener(
    private val pipEntry: () -> Unit = {},
    private val pipRestore: () -> Unit = {},
    private val pipPlayPrev: () -> Unit = {},
    private val pipPlayNext: () -> Unit = {},
    private val pipPlayError: () -> Unit = {},
) : OnVideoListener {
    override fun onPipEntry() {
        pipEntry()
    }

    override fun onPipRestore() {
        pipRestore()
    }

    override fun onPipPlayPrev() {
        pipPlayPrev()
    }

    override fun onPipPlayNext() {
        pipPlayNext()
    }

    override fun onPipPlayError() {
        pipPlayError()
    }
}