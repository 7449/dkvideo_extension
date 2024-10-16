package xyz.doikki.videoplayer.pipextension.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import xyz.doikki.videoplayer.pipextension.SimpleVideoPlayActivity
import xyz.doikki.videoplayer.pipextension.listPipVideo
import xyz.doikki.videoplayer.pipextension.singlePipVideo

class SampleActivity : AppCompatActivity(R.layout.sample_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.single).setOnClickListener {
            SimpleVideoPlayActivity.single(SampleUrls.item)
        }
        findViewById<View>(R.id.list).setOnClickListener {
            SimpleVideoPlayActivity.list(SampleUrls.items)
        }
        findViewById<View>(R.id.single_pip).setOnClickListener {
            singlePipVideo(SampleUrls.item)
        }
        findViewById<View>(R.id.list_pip).setOnClickListener {
            listPipVideo(SampleUrls.items)
        }
    }

}