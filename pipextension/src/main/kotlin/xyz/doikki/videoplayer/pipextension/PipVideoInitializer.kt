package xyz.doikki.videoplayer.pipextension

import android.annotation.SuppressLint
import android.content.Context
import androidx.startup.Initializer

class PipVideoInitializer : Initializer<Unit> {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var _context: Context
        val appContext get() = _context
    }

    override fun create(context: Context) {
        _context = context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}