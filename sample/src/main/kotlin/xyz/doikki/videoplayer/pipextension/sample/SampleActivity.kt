package xyz.doikki.videoplayer.pipextension.sample

import android.os.Bundle
import android.view.View
import xyz.doikki.videoplayer.pipextension.SimpleVideoPlayActivity
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoActivity

class SampleActivity : SimpleVideoActivity(R.layout.sample_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.single).setOnClickListener {
            SimpleVideoPlayActivity.single(SampleUrls.item)
        }
        findViewById<View>(R.id.multi).setOnClickListener {
            SimpleVideoPlayActivity.list(SampleUrls.items)
        }
    }

}