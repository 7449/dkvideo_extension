package xyz.doikki.videoplayer.pipextension.simple

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import xyz.doikki.videoplayer.pipextension.VideoManager

class SimpleVideoLifecycle : DefaultLifecycleObserver {

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        VideoManager.instance.onPause()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        VideoManager.instance.onResume()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        VideoManager.instance.onDestroy()
    }

}