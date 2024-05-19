package xyz.doikki.videoplayer.pipextension

import android.annotation.SuppressLint
import android.content.Context
import xyz.doikki.videoplayer.pipextension.media3.Media3ExoFactory
import xyz.doikki.videoplayer.player.ProgressManager
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager

@SuppressLint("StaticFieldLeak")
object VideoInitializer {

    private lateinit var _context: Context
    val appContext get() = _context

    fun initializer(context: Context) {
        _context = context.applicationContext
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
            val item = selectVideoItem ?: return
            progressSp.edit().putLong(item.key, progress).apply()
        }

        override fun getSavedProgress(url: String): Long {
            val item = selectVideoItem ?: return 0
            return progressSp.getLong(item.key, 0)
        }

    }

}