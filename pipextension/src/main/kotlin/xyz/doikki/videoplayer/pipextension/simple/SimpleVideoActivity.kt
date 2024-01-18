package xyz.doikki.videoplayer.pipextension.simple

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import xyz.doikki.videoplayer.pipextension.OnVideoListener
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.isOverlayPermissions
import xyz.doikki.videoplayer.pipextension.launchOverlay

abstract class SimpleVideoActivity(layout: Int = 0) : AppCompatActivity(layout),
    OnVideoListener {

    private val typeOverlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && isOverlayPermissions()) {
                entryPipMode()
            }
        }

    protected val videoManager = VideoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, true) {
            if (!videoManager.onBackPressed()) {
                finish()
            }
        }
        if (isComeBackActivity()) {
            onAttachVideoToView()
        }
    }

    /**
     * 添加至RootView
     */
    abstract fun onAttachVideoToView()

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
        typeOverlayLauncher.unregister()
    }

    override fun onEntryPipMode() {
        entryPipMode()
    }

    override fun onPipComeBackActivity() {
        startActivity(intent)
    }

    override fun onVideoPlayError() {
    }

    override fun onVideoPlayNext() {
    }

    override fun onVideoPlayPrev() {
    }

    private fun entryPipMode() {
        if (!isOverlayPermissions()) {
            typeOverlayLauncher.launchOverlay()
        } else {
            videoManager.attachWindow()
            finish()
        }
    }

    private fun isComeBackActivity(): Boolean {
        if (!videoManager.isOverlay) return false
        videoManager.setVideoListener(this)
        return true
    }

    protected fun playVideo(
        url: String,
        tag: String,
        title: String,
        container: ViewGroup?,
    ) {
        videoManager.setVideoListener(this)
        videoManager.showAnimView()
        videoManager.attachVideo(container, tag, title)
        videoManager.showVideoView()
        videoManager.startVideo(url)
    }

    protected suspend fun playVideo(
        tag: String,
        title: String,
        container: ViewGroup?,
        scope: suspend () -> String,
    ) {
        videoManager.setVideoListener(this)
        videoManager.showAnimView()
        videoManager.attachVideo(container, tag, title)
        val url = scope.invoke()
        videoManager.showVideoView()
        videoManager.startVideo(url)
    }

}