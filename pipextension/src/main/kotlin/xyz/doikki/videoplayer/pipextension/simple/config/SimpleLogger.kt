package xyz.doikki.videoplayer.pipextension.simple.config

import android.util.Log

object SimpleLogger {

    private const val TAG = "SimpleVideo"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun e(msg: String) {
        Log.e(TAG, msg)
    }

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

}