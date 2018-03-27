package com.rubius.rwatchlocator

import android.graphics.Path
import com.snatik.polygon.Point

/**
 *
 */
class Room(val name: String, points: List<Point>) {
    var path: Path? = null
    val lines = arrayListOf<NormalLine>()

    init {
        var prevPoint: Point? = null
        for (point in points) {
            if (prevPoint != null)
                lines.add(NormalLine(prevPoint.x, prevPoint.y, point.x, point.y, this))
            prevPoint = point
        }
        if (lines.size > 1) {
            val startLine = lines[0]
            lines.add(NormalLine(prevPoint!!.x, prevPoint.y, startLine.startX, startLine.startY, this))
        }
    }

    fun createPath() {
        if (this.path != null)
            return

        val path = Path()
        for ((i, line) in lines.withIndex()) {
            if (i == 0)
                path.moveTo(line.startX.toFloat(), line.startY.toFloat())
            path.lineTo(line.endX.toFloat(), line.endY.toFloat())
            //path.close()
        }
        this.path = path
    }

    override fun toString(): String {
        return "Room(name='$name', path=$lines)"
    }
}
