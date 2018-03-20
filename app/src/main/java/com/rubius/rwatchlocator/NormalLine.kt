package com.rubius.rwatchlocator

import com.snatik.polygon.Line

/**
 *
 */
data class NormalLine(val startX: Double, val startY: Double, val endX: Double, val endY: Double) {
    constructor(line: Line) : this(line.start.x, line.start.y, line.end.x, line.end.y)

    val normal = Vector(-(endY - startY), endX - startX).normalize()

    fun getSide(x: Double, y: Double): Side {
        val dot = normal.dot(x - startX, y - startY)
        return when {
            dot < 0.0001 -> Side.BACK
            dot > 0.0001 -> Side.FRONT
            else -> Side.COLLINEAR
        }
    }

    override fun toString(): String {
        return "NormalLine(startX=$startX, startY=$startY, endX=$endX, endY=$endY, normal=$normal)"
    }

    enum class Side {
        BACK, COLLINEAR, FRONT
    }
}
