package xyz.doikki.videoplayer.pipextension.initializer

import android.annotation.SuppressLint
import android.content.Context
import xyz.doikki.videoplayer.pipextension.VideoManager
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
            if (VideoManager.videoTag.isNullOrBlank()) return
            progressSp.edit().putLong(VideoManager.videoTag, progress).apply()
        }

        override fun getSavedProgress(url: String): Long {
            return progressSp.getLong(VideoManager.videoTag, 0)
        }

    }

}