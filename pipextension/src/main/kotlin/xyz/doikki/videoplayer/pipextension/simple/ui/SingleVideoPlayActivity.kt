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
        private const val HEADER = "HEADER"
        fun start(context: Context, url: String, title: String, header: Bundle = Bundle.EMPTY) {
            context.startActivity(Intent(context, SingleVideoPlayActivity::class.java).apply {
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                putExtra(URL, url)
                putExtra(TITLE, title)
                putExtra(HEADER, header)
            })
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val title by lazy { intent.getStringExtra(TITLE).orEmpty() }
    private val url by lazy { intent.getStringExtra(URL).orEmpty() }
    private val header by lazy { intent.getBundleExtra(HEADER) ?: Bundle.EMPTY }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        playVideo(
            url,
            url,
            url,
            findViewById<FrameLayout>(R.id.video),
            header = bundleToMap(header)
        )
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

    private fun bundleToMap(bundle: Bundle): Map<String, String> {
        val map = mutableMapOf<String, String>()
        for (key in bundle.keySet()) {
            @Suppress("DEPRECATION")
            val value = bundle.get(key)
            map[key] = value.toString()
        }
        return map
    }

}