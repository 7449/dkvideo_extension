package xyz.doikki.videoplayer.pipextension.sample

import xyz.doikki.videoplayer.pipextension.SimpleVideoItem

object SampleUrls {

    private const val W3C = "https://www.w3schools.com/html/movie.mp4"
    private const val W3C_H5 = "http://www.w3school.com.cn/example/html5/mov_bbb.mp4"
    private const val TRAILER = "https://media.w3.org/2010/05/sintel/trailer.mp4"

    val item = SimpleVideoItem.create(
        title = "Video",
        url = W3C_H5,
        key = W3C_H5,
    )

    val items = arrayListOf<SimpleVideoItem>().apply {
        add(
            SimpleVideoItem.create(
                title = "Video1",
                url = W3C,
                key = W3C,
            )
        )
        add(
            SimpleVideoItem.create(
                title = "Video2",
                url = W3C_H5,
                key = W3C_H5,
                select = true
            )
        )
        add(
            SimpleVideoItem.create(
                title = "Video3",
                url = TRAILER,
                key = TRAILER,
            )
        )
    }

}