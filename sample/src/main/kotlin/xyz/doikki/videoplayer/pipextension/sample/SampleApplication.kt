package xyz.doikki.videoplayer.pipextension.sample

import android.app.Application
import android.content.Intent
import android.view.ViewGroup
import android.widget.Toast
import xyz.doikki.videoplayer.pipextension.OnVideoListener
import xyz.doikki.videoplayer.pipextension.initializer.VideoInitializer
import xyz.doikki.videoplayer.pipextension.scanActivity
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoActivity

class SampleApplication : Application(), OnVideoListener {

    override fun onCreate() {
        super.onCreate()
        VideoInitializer.initializer(this)
    }

    override fun onSwitchPipMode(parent: ViewGroup?) {
        val viewGroup = parent ?: return
        val scanActivity = viewGroup.scanActivity()
        if (scanActivity is SimpleVideoActivity) {
            scanActivity.switchPipMode()
        }
    }

    override fun onPipComeBackActivity(parent: ViewGroup?) {
        startActivity(Intent(this, SamplePlayActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    override fun onVideoPlayPrev(parent: ViewGroup?) {
        Toast.makeText(this, "PrevVideo", Toast.LENGTH_SHORT).show()
    }

    override fun onVideoPlayNext(parent: ViewGroup?) {
        Toast.makeText(this, "NextVideo", Toast.LENGTH_SHORT).show()
    }

    override fun onVideoPlayError(parent: ViewGroup?) {
    }

}