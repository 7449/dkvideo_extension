package xyz.doikki.videoplayer.pipextension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import xyz.doikki.videoplayer.util.PlayerUtils

private const val TYPE_SYSTEM_ALERT_COMPATIBLE = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
private val metrics get() = Resources.getSystem().displayMetrics

internal fun ViewGroup.scanActivity(): Activity {
    return PlayerUtils.scanForActivity(this.context)
}

internal fun Float.dp2pxInt(): Int =
    dp2px().toInt()

internal fun Float.dp2px(): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)

internal val Context.statusBarHeight: Int
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    get() {
        return runCatching {
            val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
            resources.getDimensionPixelSize(resourceId)
        }.getOrNull() ?: 0
    }

internal fun WindowManager.LayoutParams.floatType() = also {
    type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else TYPE_SYSTEM_ALERT_COMPATIBLE
}

internal val Context.isLandscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun Context.isOverlays(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(this)
    } else {
        return true
    }
}

fun ActivityResultLauncher<Intent>.launchOverlay(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        launch(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
        )
    }
}

class PipVideoManagerLifecycle : DefaultLifecycleObserver {

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.e("Print", "Video onPause ${PipVideoManager.instance.isPipMode}")
        PipVideoManager.instance.onPause()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.e("Print", "Video onResume ${PipVideoManager.instance.isPipMode}")
        PipVideoManager.instance.onResume()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.e("Print", "Video onDestroy ${PipVideoManager.instance.isPipMode}")
        PipVideoManager.instance.onDestroy()
    }

}
