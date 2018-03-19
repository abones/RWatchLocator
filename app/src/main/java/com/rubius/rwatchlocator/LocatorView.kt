package com.rubius.rwatchlocator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

/**
 *
 */
class LocatorView(
        context: Context,
        attrs: AttributeSet?) : View(context, attrs) {
    private val circlePaint: Paint = Paint()
    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())
    private var scale = 1.0f

    companion object {
        const val MIN_SCALE = 0.1f
        const val MAX_SCALE = 100.0f
    }

    init {
        circlePaint.color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()

        canvas.scale(scale, scale)
        canvas.translate((storedTranslationX + activeTranslationX) / scale, (storedTranslationY + activeTranslationY) / scale)
        canvas.drawCircle(50f, 50f, 100f, circlePaint)

        canvas.restore()
    }

    private var isTranslating = false

    private var startX: Float = 0.0f
    private var startY: Float = 0.0f
    private var storedTranslationX: Float = 0.0f
    private var storedTranslationY: Float = 0.0f
    private var activeTranslationX: Float = 0.0f
    private var activeTranslationY: Float = 0.0f

    private fun updateTranslation(event: MotionEvent, isUpEvent: Boolean) {
        var newIsTranslating = /*event.pointerCount == 1 && */!isUpEvent

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
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return false

        when (event.actionMasked) {
            MotionEvent.ACTION_UP -> updateTranslation(event, true)
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_POINTER_DOWN -> updateTranslation(event, false)
            MotionEvent.ACTION_MOVE -> {
                if (isTranslating) {
                    activeTranslationX = event.x - startX
                    activeTranslationY = event.y - startY
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
            return true
        }
    }
}

fun Float.clamp(min: Float, max: Float): Float {
    if (this < min)
        return min
    else if (this > max)
        return max
    return this
}
