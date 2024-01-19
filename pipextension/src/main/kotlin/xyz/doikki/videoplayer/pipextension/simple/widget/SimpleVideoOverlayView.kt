package xyz.doikki.videoplayer.pipextension.simple.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.PointF
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.widget.FrameLayout
import xyz.doikki.videoplayer.pipextension.R
import xyz.doikki.videoplayer.pipextension.overlayType
import xyz.doikki.videoplayer.pipextension.statusBarHeight
import xyz.doikki.videoplayer.util.PlayerUtils
import kotlin.math.abs

class SimpleVideoOverlayView @JvmOverloads constructor(
    context: Context, downX: Int = 50, downY: Int = 50,
) : FrameLayout(context) {

    private val windowManager = PlayerUtils.getWindowManager(context)
    private val layoutParams = WindowManager.LayoutParams()
    private val downRawPointF = PointF(0f, 0f)
    private val downPoint = Point(downX, downY)
    private var isOverlay = false

    init {
        setBackgroundColor(Color.TRANSPARENT)
        layoutParams.overlayType()
        layoutParams.format = PixelFormat.TRANSLUCENT
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.windowAnimations = R.style.ThemeSampleFloatWindowAnimation
        layoutParams.gravity = Gravity.START or Gravity.TOP
        val width = (resources.displayMetrics.widthPixels / 1.5).toInt()
        layoutParams.width = width
        layoutParams.height = width * 9 / 16
        layoutParams.x = downPoint.x
        layoutParams.y = downPoint.y
    }

    fun updateViewLayout(widthAndHeight: IntArray) {
        layoutParams.width = widthAndHeight.first()
        layoutParams.height = widthAndHeight.last()
        windowManager.updateViewLayout(this, layoutParams)
    }

    fun isOverlay(): Boolean {
        return isOverlay
    }

    fun addToWindow(view: View) {
        addView(view)
        if (!isAttachedToWindow) {
            windowManager.addView(this, layoutParams)
        }
        isOverlay = true
    }

    fun removeFromWindow() {
        removeAllViews()
        if (isAttachedToWindow) {
            windowManager.removeViewImmediate(this)
        }
        isOverlay = false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                downRawPointF.set(ev.rawX, ev.rawY)
                downPoint.set(ev.x.toInt(), (ev.y + context.statusBarHeight).toInt())
            }

            MotionEvent.ACTION_MOVE -> {
                val absDeltaX = abs(ev.rawX - downRawPointF.x)
                val absDeltaY = abs(ev.rawY - downRawPointF.y)
                intercepted = absDeltaX > ViewConfiguration.get(context).scaledTouchSlop
                        || absDeltaY > ViewConfiguration.get(context).scaledTouchSlop
            }
        }
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE) {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            layoutParams.x = x - downPoint.x
            layoutParams.y = y - downPoint.y
            windowManager.updateViewLayout(this, layoutParams)
        }
        return super.onTouchEvent(event)
    }

}