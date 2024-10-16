package xyz.doikki.videoplayer.pipextension.simple.develop

import xyz.doikki.videoplayer.pipextension.SimpleVideoItem
import xyz.doikki.videoplayer.pipextension.VideoItemManager
import xyz.doikki.videoplayer.pipextension.VideoManager

interface SimpleVideoListener {

    val currentSelectVideoItem: SimpleVideoItem?
    val isSingleVideo: Boolean
    fun onClosePip()
    fun onToPip()
    fun onComeBackActivity()
    fun onClickNext()
    fun onClickPrev()
    fun onClickRotation()
    fun onClickScale()

    object Default : SimpleVideoListener {
        override val currentSelectVideoItem: SimpleVideoItem? get() = VideoItemManager.selectVideoItem
        override val isSingleVideo: Boolean get() = VideoItemManager.isSingle

        override fun onClosePip() {
            VideoManager.closePip()
        }

        override fun onToPip() {
            VideoManager.toPip()
        }

        override fun onComeBackActivity() {
            VideoManager.comeBackActivity()
        }

        override fun onClickNext() {
            VideoManager.videoPlayNext()
        }

        override fun onClickPrev() {
            VideoManager.videoPlayPrev()
        }

        override fun onClickRotation() {
            VideoManager.refreshRotation()
        }

        override fun onClickScale() {
            VideoManager.refreshScreenScale()
        }
    }

}