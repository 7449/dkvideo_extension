package xyz.doikki.videoplayer.pipextension

internal interface OnVideoListener {
    fun onEntryPip()
    fun onEntryActivity()
    fun onVideoPlayPrev()
    fun onVideoPlayNext()
    fun onVideoPlayError()
}

interface VideoUrlCallback {
    fun onRequestVideoUrl(action: (url: String) -> Unit)
}

data class SimpleVideoItem(
    val title: String,
    val urlCallback: VideoUrlCallback,
    val header: Map<String, String> = emptyMap(),
    val cover: Any? = null,
    val placeholder: Int? = null,
    val key: String = title,
    var select: Boolean = false,
) {
    companion object {
        fun create(
            title: String = "",
            url: String? = null,
            urlCallback: VideoUrlCallback? = null,
            header: Map<String, String> = emptyMap(),
            cover: Any? = null,
            placeholder: Int? = null,
            key: String = title,
            select: Boolean = true,
        ): SimpleVideoItem {
            return SimpleVideoItem(
                title = title,
                urlCallback = urlCallback ?: object : VideoUrlCallback {
                    override fun onRequestVideoUrl(action: (String) -> Unit) {
                        action.invoke(url.orEmpty())
                    }
                },
                header = header,
                cover = cover,
                placeholder = placeholder,
                key = key,
                select = select
            )
        }
    }
}

private val simpleVideoItems = arrayListOf<SimpleVideoItem>()

internal object VideoItemManager {
    private val selectVideoItemIndex get() = simpleVideoItems.indexOfFirst { it.select }
    private val videoItemSize get() = simpleVideoItems.size
    val allVideoItems get() = simpleVideoItems

    val prevVideoItem get() = simpleVideoItems.subList(0, selectVideoItemIndex).lastOrNull()
    val nextVideoItem get() = simpleVideoItems.drop(selectVideoItemIndex + 1).firstOrNull()
    val isSingle get() = videoItemSize <= 1
    val selectVideoItem get() = simpleVideoItems.find { it.select }

    fun refreshItem(item: List<SimpleVideoItem>) {
        val newItem = item.map { it.copy() }
        simpleVideoItems.clear()
        simpleVideoItems.addAll(newItem)
    }

    fun refreshState(item: SimpleVideoItem, callback: () -> Unit) {
        simpleVideoItems.forEach { it.select = it.key == item.key }
        if (!isSingle) {
            callback.invoke()
        }
    }
}