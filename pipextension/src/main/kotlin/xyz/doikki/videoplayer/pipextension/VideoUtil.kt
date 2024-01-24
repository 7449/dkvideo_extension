package xyz.doikki.videoplayer.pipextension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isGone
import androidx.core.view.isVisible
import xyz.doikki.videoplayer.pipextension.initializer.VideoInitializer
import xyz.doikki.videoplayer.util.PlayerUtils

@Suppress("DEPRECATION")
private const val TYPE_SYSTEM_ALERT_COMPATIBLE = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

fun ViewGroup.scanActivity(): Activity = PlayerUtils.scanForActivity(context)

fun ViewGroup.scanActivityOrNull(): Activity? = PlayerUtils.scanForActivity(context)

fun Float.dp2px(): Int = PlayerUtils.dp2px(VideoInitializer.appContext, this)

val Context.statusBarHeight: Int get() = PlayerUtils.getStatusBarHeight(this).toInt()

val Context.isLandscape: Boolean get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun WindowManager.LayoutParams.overlayType() = also {
    type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    else TYPE_SYSTEM_ALERT_COMPATIBLE
}

fun Context.isOverlayPermissions(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    Settings.canDrawOverlays(this)
else true

fun ActivityResultLauncher<Intent>.launchOverlay() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        launch(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${VideoInitializer.appContext.packageName}")
            )
        )
    }
}

fun Activity.fullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = window.insetsController
        controller?.hide(
            WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
        )
        controller?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}

inline fun <reified T : View> View.parentView(): T? = if (parent is T) parent as T else null

fun View.removeViewFormParent() {
    val parent = parent
    if (parent is ViewGroup) {
        parent.removeView(this)
    }
}

fun View.visible() = let { if (!isVisible) visibility = View.VISIBLE }
fun View.gone() = let { if (!isGone) visibility = View.GONE }

val videoMatchParams: ViewGroup.LayoutParams
    get() = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )