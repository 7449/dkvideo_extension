package xyz.doikki.videoplayer.pipextension.sample

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import xyz.doikki.videoplayer.pipextension.isOverlays
import xyz.doikki.videoplayer.pipextension.launchOverlay

abstract class PermissionActivity(id: Int) : AppCompatActivity(id) {

    private val typeOverlayLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && isOverlays()) {
                videoEntryPip()
            }
        }

    protected fun entryPip() {
        if (!isOverlays()) {
            typeOverlayLauncher.launchOverlay(this)
        } else {
            videoEntryPip()
        }
    }

    protected abstract fun videoEntryPip()

}