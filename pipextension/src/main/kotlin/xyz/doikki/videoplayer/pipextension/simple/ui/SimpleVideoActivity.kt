package xyz.doikki.videoplayer.pipextension.simple.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.isOverlayPermissions
import xyz.doikki.videoplayer.pipextension.overlayLaunch

abstract class SimpleVideoActivity(layout: Int = 0) : AppCompatActivity(layout) {

    private val typeOverlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && isOverlayPermissions()) {
                switchPipMode()
            }
        }

    private val videoManager = VideoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!videoManager.onBackPressed()) {
                    onBackPressedCallback()
                }
            }
        })
    }

    open fun onBackPressedCallback() {
        finish()
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
        typeOverlayLauncher.unregister()
    }

    fun switchPipMode() {
        if (!isOverlayPermissions()) {
            typeOverlayLauncher.overlayLaunch()
        } else {
            videoManager.attachParent(release = false)
            finish()
        }
    }

}