package xyz.doikki.videoplayer.pipextension.sample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import xyz.doikki.videoplayer.pipextension.PipVideoManager
import xyz.doikki.videoplayer.pipextension.listener.OnPipListener

class SampleActivity : AppCompatActivity(R.layout.sample_activity), OnPipListener {

    companion object {
        private const val URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
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
        if (!onChangeUI()) {
            onPlayerClickItem(URL)
        }
        findViewById<View>(R.id.btn_pip).setOnClickListener { showFloat() }
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

    override fun onPipRestore() {
        startActivity(Intent(this, SampleActivity::class.java))
    }

    override fun onVideoPlayPrev() {
    }

    override fun onVideoPlayNext() {
    }

    override fun onVideoPlayError() {
    }

    private fun onPlayerClickItem(url: String, isTouch: Boolean = false) {
        val rootView = if (!videoManager.isPipFloatView || isTouch) {
            videoView
        } else null
        videoManager.registerListener(this)
        videoManager.showProgressView()
        videoManager.preLoadVideo(rootView, isTouch, url, url)
        videoManager.showVideoView()
        videoManager.startVideo(url)
    }

    private fun onChangeUI(): Boolean {
        if (!videoManager.isPipFloatView) return false
        videoManager.registerListener(this)
        videoManager.addView(videoView, "")
        return true
    }

    private fun showFloat() {
        if (!isOverlays()) {
            typeOverlayLauncher.launch(
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            )
        } else {
            videoManager.addWindow()
            finish()
        }
    }

    private fun isOverlays(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    private val typeOverlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && isOverlays()) {
                showFloat()
            }
        }

}