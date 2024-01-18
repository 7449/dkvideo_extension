package xyz.doikki.videoplayer.pipextension

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

abstract class PipVideoActivity : AppCompatActivity(), OnPipManagerListener {

    private val typeOverlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && isOverlays()) {
                entryPipMode()
            }
        }

    protected val videoManager = PipVideoManager.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, true) {
            if (!videoManager.onBackPressed()) {
                finish()
            }
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
        typeOverlayLauncher.unregister()
    }

    override fun onPipEntry() {
        entryPipMode()
    }

    override fun onPipPlayError() {
    }

    private fun entryPipMode() {
        if (!isOverlays()) {
            typeOverlayLauncher.launchOverlay(this)
        } else {
            videoManager.attachWindow()
            finish()
        }
    }

}