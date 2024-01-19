package xyz.doikki.videoplayer.pipextension.simple

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.isOverlayPermissions
import xyz.doikki.videoplayer.pipextension.launchOverlay

abstract class SimpleVideoActivity(layout: Int = 0) : AppCompatActivity(layout) {

    private val typeOverlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && isOverlayPermissions()) {
                switchPipMode()
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

    fun switchPipMode() {
        if (!isOverlayPermissions()) {
            typeOverlayLauncher.launchOverlay()
        } else {
            videoManager.attachWindow()
            finish()
        }
    }

    protected open fun isComeBackActivity(): Boolean {
        return videoManager.isOverlay
    }

    protected fun playVideo(
        url: String,
        tag: String,
        title: String,
        parent: ViewGroup?,
    ) {
        videoManager.showAnimView()
        videoManager.attachParent(parent, tag, title)
        videoManager.showVideoView()
        videoManager.startVideo(url)
    }

    protected suspend fun playVideo(
        tag: String,
        title: String,
        parent: ViewGroup?,
        scope: suspend () -> String,
    ) {
        videoManager.showAnimView()
        videoManager.attachParent(parent, tag, title)
        val url = scope.invoke()
        videoManager.showVideoView()
        videoManager.startVideo(url)
    }

}