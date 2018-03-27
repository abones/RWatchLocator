package com.rubius.rwatchlocator

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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
    private val roomPaint = Paint()
    var database: Database? = null

    var minLevel: Int = 0
        set (value) {
            field = value
            invalidate()
        }

    var maxLevel: Int = 0
        set (value) {
            field = value
            invalidate()
        }

    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())
    private val longPressDetector = GestureDetector(context, LongPressListener())

    private val drawMatrix = Matrix()
    private val drawPath = Path()

    companion object {
        const val MIN_SCALE = 0.1f
        const val MAX_SCALE = 100.0f
        const val LINE_PADDING = 100.0f // pixels
        const val LINE_LENGTH = 100.0f // pixels
    }

    init {
        circlePaint.color = Color.RED
        debugPaint.color = Color.GREEN
        roomPaint.color = Color.BLACK
        roomPaint.style = Paint.Style.STROKE
        roomPaint.strokeWidth = 2.0f
        //roomPaint.shader =  LinearGradient(0.0f, 0.0f, 50.0f, 50.0f, Color.RED, Color.GREEN, Shader.TileMode.MIRROR)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        prescaler = (Math.min(w, h) / LINE_LENGTH) * 10.0f
    }

    private fun drawNode(canvas: Canvas, node: TreeNode?, level: Int) {
        if (node == null)
            return

        if (level in minLevel..maxLevel) {
            drawNodeLines(node.convexLines, canvas, Color.WHITE)

            val color = colors[level % colors.size]
            drawNodeLines(node.lines, canvas, color)
        }

        drawNode(canvas, node.front, level + 1)
        drawNode(canvas, node.back, level + 1)
    }

    private fun drawNodeLines(lines: List<NormalLine>, canvas: Canvas, color: Int) {
        for (line in lines) {
            debugPaint.strokeWidth = 0.5f
            debugPaint.color = color
            canvas.drawLine(line.startX.toFloat(), line.startY.toFloat(), line.endX.toFloat(), line.endY.toFloat(), debugPaint)

            // draw normal
            canvas.save()

            canvas.translate((line.endX + line.startX).toFloat() / 2.0f, (line.endY + line.startY).toFloat() / 2.0f)
            canvas.scale(line.length.toFloat() * 0.1f, line.length.toFloat() * 0.1f)

            debugPaint.strokeWidth = 0.0f
            debugPaint.color = Color.RED
            canvas.drawLine(0.0f, 0.0f, line.normal.x.toFloat(), line.normal.y.toFloat(), debugPaint)

            canvas.restore()
        }
    }

    private fun drawScene(canvas: Canvas) {
        val totalScale = getTotalScale()

        drawMatrix.reset()
        drawMatrix.setTranslate(getTotalTranslationX(), getTotalTranslationY())
        drawMatrix.postScale(totalScale, totalScale)

        canvas.save()

        canvas.matrix = drawMatrix

        debugPaint.strokeWidth = 0.0f
        debugPaint.color = Color.DKGRAY
        canvas.drawLine(0.0f, 0.0f, 1.0f, 0.0f, debugPaint)
        canvas.drawLine(0.0f, 0.0f, 0.0f, 1.0f, debugPaint)

        drawNode(canvas, database!!.bspRoot, 0)

        for (anchorPoint in database!!.anchorPoints)
            canvas.drawCircle(anchorPoint.x.toFloat(), anchorPoint.y.toFloat(), 0.5f, circlePaint)

        canvas.restore()

        for (room in database!!.rooms) {
            room.path!!.transform(drawMatrix, drawPath)
            //roomPaint.shader.setLocalMatrix(drawMatrix)
            canvas.drawPath(drawPath, roomPaint)
        }
    }

    private fun drawOverlay(canvas: Canvas) {
        val lineX = width - LINE_PADDING
        val lineY = height - LINE_PADDING
        canvas.drawLine(lineX - LINE_LENGTH, lineY, lineX, lineY, debugPaint)
        canvas.drawText("${screenToWorld(LINE_LENGTH)}", lineX - LINE_LENGTH, lineY - 100.0f, debugPaint)
    }

    override fun onDraw(canvas: Canvas) {
        drawScene(canvas)
        drawOverlay(canvas)
    }

    private val colors = arrayOf(
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.CYAN,
        Color.MAGENTA,
        Color.YELLOW,
        Color.DKGRAY,
        Color.LTGRAY
    )

    private var prescaler = 1.0f
    private var scale = 1.0f

    private fun getTotalScale(): Float {
        return prescaler * scale
    }

    private fun worldToScreen(coordWorld: Float): Float {
        return coordWorld * getTotalScale()
    }

    private fun screenToWorld(coordScreen: Float): Float {
        return coordScreen / getTotalScale()
    }

    private fun screenToWorldX(coordScreen: Float): Float {
        return screenToWorld(coordScreen) - getTotalTranslationX()
    }

    private fun screenToWorldY(coordScreen: Float): Float {
        return screenToWorld(coordScreen) - (getTotalTranslationY())
    }

    private var isTranslating = false

    private var startX: Float = 0.0f
    private var startY: Float = 0.0f
    private var storedTranslationX: Float = 0.0f
    private var storedTranslationY: Float = 0.0f
    private var activeTranslationX: Float = 0.0f
    private var activeTranslationY: Float = 0.0f

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
        listener?.invoke(getTotalScale(), getTotalTranslationX(), getTotalTranslationY())
    }

    private fun getTotalTranslationY() = storedTranslationY + activeTranslationY

    private fun getTotalTranslationX() = storedTranslationX + activeTranslationX

    inner class LongPressListener : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent?) {
            if (e == null)
                return

            val x = screenToWorldX(e.x)
            val y = screenToWorldY(e.y)
            database?.anchorPoints?.add(AnchorPoint(x.toDouble(), y.toDouble()))
            invalidate()
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
