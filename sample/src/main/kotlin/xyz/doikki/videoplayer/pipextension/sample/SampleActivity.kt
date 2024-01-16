package xyz.doikki.videoplayer.pipextension.sample

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.addCallback
import xyz.doikki.videoplayer.pipextension.PipVideoManager
import xyz.doikki.videoplayer.pipextension.listener.OnPipManagerListener

class SampleActivity : PermissionActivity(R.layout.sample_activity), OnPipManagerListener {

    companion object {
        private const val URL = "https://www.w3schools.com/html/movie.mp4"
    }

    private val videoManager = PipVideoManager.instance.isPlayList(false)
    private val videoView by lazy { findViewById<FrameLayout>(R.id.video) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, true) {
            if (!videoManager.onBackPressed()) {
                finish()
            }
        }
        if (!isPipStartActivity()) {
            onPlayerClickItem(URL)
        }
    }

    override fun onPause() {
        super.onPause()
        videoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        videoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoManager.onDestroy()
    }

    override fun videoEntryPip() {
        videoManager.attachWindow()
        finish()
    }

    override fun onPipEntry() {
        entryPip()
    }

    override fun onPipRestore() {
        startActivity(intent)
    }

    override fun onPipPlayPrev() {
    }

    override fun onPipPlayNext() {
    }

    override fun onPipPlayError() {
    }

    private fun onPlayerClickItem(url: String, isTouch: Boolean = false) {
        val rootView = if (!videoManager.isPipMode || isTouch) videoView
        else null
        videoManager.setPipManagerListener(this)
        videoManager.showProgressView()
        videoManager.attachVideo(rootView, url, url)
        videoManager.showVideoView()
        videoManager.startVideo(url)
    }

    private fun isPipStartActivity(): Boolean {
        if (!videoManager.isPipMode) return false
        videoManager.setPipManagerListener(this)
        videoManager.attachView(videoView, "")
        return true
    }

}