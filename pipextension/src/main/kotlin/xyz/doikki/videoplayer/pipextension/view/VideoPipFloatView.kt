package xyz.doikki.videoplayer.pipextension.view

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
import xyz.doikki.videoplayer.pipextension.floatType
import xyz.doikki.videoplayer.pipextension.statusBarHeight
import xyz.doikki.videoplayer.util.PlayerUtils
import kotlin.math.abs

@SuppressLint("WrongConstant")
internal class VideoPipFloatView @JvmOverloads constructor(
    context: Context, downX: Int = 50, downY: Int = 50,
) : FrameLayout(context) {

    private val manager = PlayerUtils.getWindowManager(context.applicationContext)
    private val params = WindowManager.LayoutParams()
    private val downRawPointF = PointF(0f, 0f)
    private val downPoint = Point(downX, downY)
    private var isShowing = false

    init {
        setBackgroundColor(Color.TRANSPARENT)
        params.floatType()
        params.format = PixelFormat.TRANSLUCENT
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.windowAnimations = R.style.ThemeSampleFloatWindowAnimation
        params.gravity = Gravity.START or Gravity.TOP
        val width = (resources.displayMetrics.widthPixels / 1.5).toInt()
        params.width = width
        params.height = width * 9 / 16
        params.x = downPoint.x
        params.y = downPoint.y
    }

    fun updateViewLayout(widthAndHeight: IntArray) {
        params.width = widthAndHeight.first()
        params.height = widthAndHeight.last()
        manager.updateViewLayout(this, params)
    }

    fun isShow(): Boolean {
        return isShowing
    }

    fun addToWindow(view: View) {
        addView(view)
        if (!isAttachedToWindow) {
            manager.addView(this, params)
            isShowing = true
        }
    }

    fun removeFromWindow() {
        removeAllViews()
        if (isAttachedToWindow) {
            manager.removeViewImmediate(this)
            isShowing = false
        }
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE) {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            params.x = x - downPoint.x
            params.y = y - downPoint.y
            manager.updateViewLayout(this, params)
        }
        return super.onTouchEvent(event)
    }

}