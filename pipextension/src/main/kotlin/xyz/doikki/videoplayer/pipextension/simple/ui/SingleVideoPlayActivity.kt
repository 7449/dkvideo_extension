package xyz.doikki.videoplayer.pipextension.simple.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import androidx.core.os.postDelayed
import xyz.doikki.videoplayer.pipextension.R
import xyz.doikki.videoplayer.pipextension.fullScreen

class SingleVideoPlayActivity : SimpleVideoActivity(R.layout.video_layout_play_activity_single) {

    companion object {
        private const val URL = "URL"
        private const val TITLE = "TITLE"
        fun start(context: Context, url: String, title: String) {
            context.startActivity(Intent(context, SingleVideoPlayActivity::class.java).apply {
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                putExtra(URL, url)
                putExtra(TITLE, title)
            })
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val title by lazy { intent.getStringExtra(TITLE).orEmpty() }
    private val url by lazy { intent.getStringExtra(URL).orEmpty() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        playVideo(url, url, url, findViewById<FrameLayout>(R.id.video))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handler.postDelayed(200) { fullScreen() }
    }

    override fun onResume() {
        super.onResume()
        videoManager.isPlayList(false).isPip(false)
    }

    override fun onAttachVideoToView() {
        videoManager.attachView(findViewById<FrameLayout>(R.id.video), title)
    }

}