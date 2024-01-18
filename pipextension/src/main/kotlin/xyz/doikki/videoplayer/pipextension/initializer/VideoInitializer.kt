package xyz.doikki.videoplayer.pipextension.initializer

import android.annotation.SuppressLint
import android.content.Context
import androidx.startup.Initializer
import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.player.ProgressManager
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager

class VideoInitializer : Initializer<Unit> {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var _context: Context
        val appContext get() = _context
    }

    override fun create(context: Context) {
        _context = context
        createVideo()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    private fun createVideo() {
        VideoViewManager.setConfig(
            VideoViewConfig.newBuilder()
                .setPlayerFactory(ExoMediaPlayerFactory.create())
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
            if (VideoManager.instance.videoTag.isNullOrBlank()) return
            progressSp.edit().putLong(VideoManager.instance.videoTag, progress).apply()
        }

        override fun getSavedProgress(url: String): Long {
            return progressSp.getLong(VideoManager.instance.videoTag, 0)
        }

    }

}