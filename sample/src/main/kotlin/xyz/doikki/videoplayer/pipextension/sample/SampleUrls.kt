package xyz.doikki.videoplayer.pipextension.sample

import xyz.doikki.videoplayer.pipextension.SimpleVideoItem

object SampleUrls {

    val item = arrayListOf<SimpleVideoItem>().apply {
        add(
            SimpleVideoItem(
                title = "Video",
                url = { it.invoke(W3C_H5) },
                key = W3C_H5,
            )
        )
    }

    val items = arrayListOf<SimpleVideoItem>().apply {
        add(
            SimpleVideoItem(
                title = "Video1",
                url = { it.invoke(W3C) },
                cover = com.squareup.leakcanary.R.mipmap.leak_canary_icon,
                key = W3C,
            )
        )
        add(
            SimpleVideoItem(
                title = "Video2",
                url = { it.invoke(W3C_H5) },
                cover = com.squareup.leakcanary.R.mipmap.leak_canary_icon,
                key = W3C_H5,
                select = true
            )
        )
        add(
            SimpleVideoItem(
                title = "Video3",
                url = { it.invoke(TRAILER) },
                cover = com.squareup.leakcanary.R.mipmap.leak_canary_icon,
                key = TRAILER,
            )
        )
    }

    private const val W3C = "https://www.w3schools.com/html/movie.mp4"
    private const val W3C_H5 = "http://www.w3school.com.cn/example/html5/mov_bbb.mp4"
    private const val TRAILER = "https://media.w3.org/2010/05/sintel/trailer.mp4"

}