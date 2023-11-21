package xyz.doikki.videoplayer.pipextension

import android.content.Context
import xyz.doikki.videoplayer.player.ProgressManager

internal class PlayerProgressManager : ProgressManager() {

    companion object {
        private const val PLAYER_SP_NAME = "player_progress_manager"
    }

    private val playerSp by lazy {
        VideoProvider.appContext.getSharedPreferences(
            PLAYER_SP_NAME,
            Context.MODE_PRIVATE
        )
    }

    override fun saveProgress(url: String, progress: Long) {
        if (SingleVideoManager.instance.currentVideoTag.isNullOrBlank()) return
        playerSp.edit().putLong(SingleVideoManager.instance.currentVideoTag, progress).apply()
    }

    override fun getSavedProgress(url: String): Long {
        return playerSp.getLong(SingleVideoManager.instance.currentVideoTag, 0)
    }

}