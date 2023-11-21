package xyz.doikki.videoplayer.pipextension.view

import android.view.View
import android.view.ViewGroup

internal inline fun <reified T : View> View.isParentView(): Boolean {
    return parent is T
}

internal inline fun <reified T : View> View.parentView(): T? {
    if (parent is T) return parent as T
    return null
}

internal fun View.removeViewFormParent() {
    val parent = parent
    if (parent is ViewGroup) {
        parent.removeView(this)
    }
}

internal fun View.isVisible() = visibility == View.VISIBLE

internal fun View.isGone() = visibility == View.GONE

internal fun View.visible() = apply { if (!isVisible()) visibility = View.VISIBLE }

internal fun View.gone() = apply { if (!isGone()) visibility = View.GONE }

internal val matchViewGroupParams: ViewGroup.LayoutParams
    get() = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )