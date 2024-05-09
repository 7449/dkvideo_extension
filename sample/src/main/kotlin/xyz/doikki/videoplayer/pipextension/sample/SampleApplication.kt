package xyz.doikki.videoplayer.pipextension.sample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.view.ViewGroup
import android.widget.Toast
import xyz.doikki.videoplayer.pipextension.OnVideoListener
import xyz.doikki.videoplayer.pipextension.initializer.VideoInitializer
import xyz.doikki.videoplayer.pipextension.scanActivity
import xyz.doikki.videoplayer.pipextension.simple.ui.SimpleVideoActivity
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SampleApplication : Application(), OnVideoListener {

    override fun onCreate() {
        super.onCreate()
        handleSSLHandshake()
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

    @SuppressLint("CustomX509TrustManager")
    private object SimpleX509TrustManager : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }
    }

    @SuppressLint("CustomX509TrustManager")
    fun handleSSLHandshake() {
        runCatching {
            val trustAllCerts = arrayOf<TrustManager>(SimpleX509TrustManager)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
        }
    }

}