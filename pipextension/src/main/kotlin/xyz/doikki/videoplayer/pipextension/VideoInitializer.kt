package xyz.doikki.videoplayer.pipextension

import android.annotation.SuppressLint
import android.content.Context
import xyz.doikki.videoplayer.pipextension.media3.Media3ExoFactory
import xyz.doikki.videoplayer.pipextension.simple.develop.SimpleVideoListener
import xyz.doikki.videoplayer.player.ProgressManager
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager

@SuppressLint("StaticFieldLeak")
object VideoInitializer {

    private lateinit var _listener: SimpleVideoListener
    private lateinit var _context: Context
    val appContext get() = _context
    val listener get() = _listener

    fun initializer(context: Context, listener: SimpleVideoListener = SimpleVideoListener.Default) {
        _context = context.applicationContext
        _listener = listener
        createVideo()
    }

    private fun createVideo() {
        VideoViewManager.setConfig(
            VideoViewConfig.newBuilder()
                .setPlayerFactory(Media3ExoFactory.create())
                .setProgressManager(VideoTagProgressManager())
                .build()
        )
    }

    private class VideoTagProgressManager : ProgressManager() {

        companion object {
            private const val PLAYER_SP_NAME = "player_tag_progress_manager"
        }

        private val progressSp by lazy {
            appContext.getSharedPreferences(PLAYER_SP_NAME, Context.MODE_PRIVATE)
        }

        override fun saveProgress(url: String, progress: Long) {
            val item = listener.currentSelectVideoItem ?: return
            progressSp.edit().putLong(item.key, progress).apply()
        }

        override fun getSavedProgress(url: String): Long {
            val item = listener.currentSelectVideoItem ?: return 0
            return progressSp.getLong(item.key, 0)
        }

    }

}