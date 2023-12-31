package xyz.doikki.videoplayer.pipextension.view.view

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.pipextension.listener.OnVideoSizeChangedListener
import xyz.doikki.videoplayer.player.VideoView

internal class VideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr) {

    private var listener: OnVideoSizeChangedListener? = null

    fun isMp3(): Boolean {
        return mUrl.orEmpty().lowercase().contains(".mp3".lowercase())
    }

    fun url(): String? {
        return mUrl
    }

    override fun setUrl(url: String, headers: MutableMap<String, String>?) {
        super.setUrl(url, headers)
    }

    fun checkVideoSize(): Boolean {
        return videoSize.first() != 0 && videoSize.last() != 0
    }

    fun resetVideoSize() {
        mVideoSize = intArrayOf(0, 0)
    }

    fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        this.listener = listener
    }

    override fun onVideoSizeChanged(videoWidth: Int, videoHeight: Int) {
        super.onVideoSizeChanged(videoWidth, videoHeight)
        listener?.onVideoSizeChanged(videoSize)
    }

}