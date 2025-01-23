package xyz.doikki.videoplayer.pipextension.simple.config

sealed class SimpleVideoType(val type: String) {
    companion object {
        fun value(config: String): SimpleVideoType {
            return when (config) {
                Dash.type -> Dash
                Hls.type -> Hls
                Other.type -> Other
                else -> Other
            }
        }
    }

    data object Dash : SimpleVideoType("dash")
    data object Hls : SimpleVideoType("hls")
    data object Other : SimpleVideoType("other")
}
