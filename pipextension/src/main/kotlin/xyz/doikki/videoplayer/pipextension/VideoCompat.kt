@file:Suppress("DEPRECATION")

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
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import xyz.doikki.videoplayer.util.PlayerUtils

internal const val TYPE_SYSTEM_ALERT_COMPATIBLE = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

internal val videoMatchParams: ViewGroup.LayoutParams
    get() = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

internal val ViewGroup.activityOrNull: Activity? get() = PlayerUtils.scanForActivity(context)

internal val ViewGroup.activity: Activity get() = checkNotNull(activityOrNull)

internal val Float.dp: Int get() = PlayerUtils.dp2px(VideoInitializer.appContext, this)

internal val Context.statusBarHeight: Int get() = PlayerUtils.getStatusBarHeight(this).toInt()

internal val Context.isLandscape: Boolean get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

internal inline fun <reified T : View> View.parentView(): T? =
    if (parent is T) parent as T else null

internal fun View.removeViewFormParent() {
    val parent = parent
    if (parent is ViewGroup) {
        parent.removeView(this)
    }
}

internal fun WindowManager.LayoutParams.updateOverlayType() = also {
    type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    else TYPE_SYSTEM_ALERT_COMPATIBLE
}

internal fun Context.isOverlayPermissions(): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        Settings.canDrawOverlays(this)
    else true

internal fun ActivityResultLauncher<Intent>.overlayLaunch() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        launch(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${VideoInitializer.appContext.packageName}")
            )
        )
    }
}