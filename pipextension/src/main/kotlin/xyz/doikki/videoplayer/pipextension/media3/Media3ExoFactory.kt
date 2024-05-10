package xyz.doikki.videoplayer.pipextension.media3

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import xyz.doikki.videoplayer.player.PlayerFactory

class Media3ExoFactory : PlayerFactory<Media3Exo>() {
    @OptIn(UnstableApi::class)
    override fun createPlayer(context: Context): Media3Exo {
        return Media3Exo(context)
    }

    companion object {
        fun create(): Media3ExoFactory {
            return Media3ExoFactory()
        }
    }
}