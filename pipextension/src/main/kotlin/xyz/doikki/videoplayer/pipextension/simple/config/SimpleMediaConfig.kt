package xyz.doikki.videoplayer.pipextension.simple.config

sealed class SimpleMediaConfig(val config: String) {
    data object PlayType : SimpleMediaConfig("simple.media.config.play.type")
}