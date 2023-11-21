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
import xyz.doikki.videoplayer.pipextension.SingleVideoManager
import xyz.doikki.videoplayer.pipextension.listener.OnSingleVideoListener

class SampleActivity : AppCompatActivity(R.layout.sample_activity), OnSingleVideoListener {

    companion object {
        private const val URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    }

    private val singleVideo = SingleVideoManager.instance
    private val videoView by lazy { findViewById<FrameLayout>(R.id.video) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, true) {
            if (!singleVideo.onBackPressed()) {
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
        singleVideo.onPause()
    }

    override fun onResume() {
        super.onResume()
        singleVideo.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        singleVideo.onDestroy()
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
        val rootView = if (!singleVideo.isPipFloatView || isTouch) {
            videoView
        } else null
        singleVideo.registerListener(this)
        singleVideo.showProgressView()
        singleVideo.preLoadVideo(rootView, isTouch, url, url)
        singleVideo.showVideoView()
        singleVideo.startVideo(url)
    }

    private fun onChangeUI(): Boolean {
        if (!singleVideo.isPipFloatView) return false
        singleVideo.registerListener(this)
        singleVideo.addView(videoView, "")
        return true
    }

    private fun showFloat() {
        if (!isOverlays()) {
            typeOverlayLauncher.launch(
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            )
        } else {
            singleVideo.addWindow()
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