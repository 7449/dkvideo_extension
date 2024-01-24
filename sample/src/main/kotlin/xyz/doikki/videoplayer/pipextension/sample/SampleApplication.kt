package xyz.doikki.videoplayer.pipextension.sample

import android.app.Application
import android.content.Intent
import android.view.ViewGroup
import xyz.doikki.videoplayer.pipextension.OnVideoListener
import xyz.doikki.videoplayer.pipextension.scanActivity
import xyz.doikki.videoplayer.pipextension.simple.SimpleVideoActivity

class SampleApplication : Application(), OnVideoListener {

    override fun onSwitchPipMode(container: ViewGroup?) {
        val viewGroup = container ?: return
        val scanActivity = viewGroup.scanActivity()
        if (scanActivity is SimpleVideoActivity) {
            scanActivity.switchPipMode()
        }
    }

    override fun onPipComeBackActivity() {
        startActivity(Intent(this, SamplePlayActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    override fun onVideoPlayPrev() {
    }

    override fun onVideoPlayNext() {
    }

    override fun onVideoPlayError() {
    }

}