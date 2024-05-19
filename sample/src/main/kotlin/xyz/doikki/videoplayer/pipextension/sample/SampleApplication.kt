package xyz.doikki.videoplayer.pipextension.sample

import android.annotation.SuppressLint
import android.app.Application
import xyz.doikki.videoplayer.pipextension.VideoInitializer
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        handleSSLHandshake()
        VideoInitializer.initializer(this)
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