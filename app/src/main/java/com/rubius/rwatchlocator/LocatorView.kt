package com.rubius.rwatchlocator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

/**
 *
 */
class LocatorView(
        context: Context,
        attrs: AttributeSet?) : View(context, attrs) {
    private val circlePaint = Paint()
    private val debugPaint = Paint()
    private val points: ArrayList<PointF> = ArrayList()

    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())
    private val longPressDetector = GestureDetector(context, LongPressListener())

    companion object {
        const val MIN_SCALE = 0.1f
        const val MAX_SCALE = 100.0f
        const val LINE_PADDING = 100.0f // pixels
        const val LINE_LENGTH = 100.0f // pixels
    }

    init {
        circlePaint.color = Color.RED
        debugPaint.color = Color.GREEN
        points.add(PointF(50.0f, 50.0f))
    }

    var prescaler = 1.0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        prescaler = (Math.min(w, h) / LINE_LENGTH)
    }

    override fun onDraw(canvas: Canvas) {
        // scene
        canvas.save()

        canvas.translate(worldToScreen(storedTranslationX + activeTranslationX), worldToScreen(storedTranslationY + activeTranslationY))

        canvas.scale(prescaler * scale, prescaler * scale)

        canvas.drawLine(0.0f, 0.0f, 1.0f, 0.0f, debugPaint)
        canvas.drawLine(0.0f, 0.0f, 0.0f, 1.0f, debugPaint)

        for (point in points) {
            canvas.drawLine(point.x - 10.0f, point.y - 10.0f, point.x, point.y - 10.0f, debugPaint)
            canvas.drawLine(point.x - 10.0f, point.y - 10.0f, point.x - 10.0f, point.y, debugPaint)
            canvas.drawCircle(point.x, point.y, 10f, circlePaint)
        }

        canvas.restore()

        // overlay
        val lineX = width - LINE_PADDING
        val lineY = height - LINE_PADDING
        canvas.drawLine(lineX - LINE_LENGTH, lineY, lineX, lineY, debugPaint)
        canvas.drawText("${screenToWorld(LINE_LENGTH)}", lineX - LINE_LENGTH, lineY - 100.0f, debugPaint)
    }

    private fun worldToScreen(coordWorld: Float): Float {
        return coordWorld * prescaler * scale
    }

    private fun screenToWorld(coordScreen: Float): Float {
        return coordScreen / (prescaler * scale)
    }

    private fun screenToWorldX(coordScreen: Float): Float {
        return screenToWorld(coordScreen) - (storedTranslationX + activeTranslationX)
    }

    private fun screenToWorldY(coordScreen: Float): Float {
        return screenToWorld(coordScreen) - (storedTranslationY + activeTranslationY)
    }

    private var isTranslating = false

    private var startX: Float = 0.0f
    private var startY: Float = 0.0f
    private var storedTranslationX: Float = 0.0f
    private var storedTranslationY: Float = 0.0f
    private var activeTranslationX: Float = 0.0f
    private var activeTranslationY: Float = 0.0f

    private var scale = 1.0f

    private fun updateTranslation(event: MotionEvent, isUpEvent: Boolean) {
        val newIsTranslating = event.pointerCount == 1 && !isUpEvent

        if (newIsTranslating == isTranslating)
            return

        isTranslating = newIsTranslating

        if (isTranslating) {
            startX = event.x
            startY = event.y
        } else {
            storedTranslationX += activeTranslationX
            storedTranslationY += activeTranslationY
            activeTranslationX = 0.0f
            activeTranslationY = 0.0f
            notifyListener()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return false

        if (longPressDetector.onTouchEvent(event))
            return true

        when (event.actionMasked) {
            MotionEvent.ACTION_UP -> updateTranslation(event, true)
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_POINTER_DOWN -> updateTranslation(event, false)
            MotionEvent.ACTION_MOVE -> {
                if (isTranslating) {
                    activeTranslationX = screenToWorld(event.x - startX)
                    activeTranslationY = screenToWorld(event.y - startY)
                    notifyListener()
                }
            }
        }
        scaleDetector.onTouchEvent(event)

        invalidate()

        return true
    }

    fun reset() {
        scale = 1.0f
        storedTranslationX = 0.0f
        storedTranslationY = 0.0f
        activeTranslationX = 0.0f
        activeTranslationY = 0.0f
        invalidate()
    }

    inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScaleEnd(p0: ScaleGestureDetector?) {
        }

        override fun onScale(p0: ScaleGestureDetector?): Boolean {
            if (p0 == null)
                return false
            scale = (scale * p0.scaleFactor).clamp(MIN_SCALE, MAX_SCALE)
            notifyListener()
            return true
        }
    }

    fun notifyListener() {
        listener?.invoke(prescaler * scale, storedTranslationX + activeTranslationX, storedTranslationY + activeTranslationY)
    }

    inner class LongPressListener : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent?) {
            if (e != null) {
                val x = screenToWorldX(e.x)
                val y = screenToWorldY(e.y)
                Log.d("TAGGG", "${e.x},${e.y} -> $x,$y")
                points.add(PointF(x, y))
                invalidate()
            }
        }
    }

    var listener: ((scale: Float, translationX: Float, translationY: Float) -> Unit)? = null
}

fun Float.clamp(min: Float, max: Float): Float {
    if (this < min)
        return min
    else if (this > max)
        return max
    return this
}