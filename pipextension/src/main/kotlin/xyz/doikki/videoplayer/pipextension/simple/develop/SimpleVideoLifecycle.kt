package xyz.doikki.videoplayer.pipextension.simple.develop

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import xyz.doikki.videoplayer.pipextension.VideoManager

class SimpleVideoLifecycle : DefaultLifecycleObserver {

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        VideoManager.onPause()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        VideoManager.onResume()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        VideoManager.onDestroy()
    }

}