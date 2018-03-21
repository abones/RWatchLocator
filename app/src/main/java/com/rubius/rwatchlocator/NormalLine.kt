package com.rubius.rwatchlocator

import com.snatik.polygon.Line
import com.snatik.polygon.Point

/**
 *
 */
data class NormalLine(val startX: Double, val startY: Double, val endX: Double, val endY: Double) {
    constructor(line: Line) : this(line.start.x, line.start.y, line.end.x, line.end.y)

    val normal = Vector(-(endY - startY), endX - startX).normalize()

    private val slope = (endY - startY) / (endX - startX)
    private val intercept = startY - slope * startX

    fun getSide(x: Double, y: Double): Side {
        val dot = normal.dot(x - startX, y - startY)
        return when {
            dot < 0.0001 -> Side.BACK
            dot > 0.0001 -> Side.FRONT
            else -> Side.COLLINEAR
        }
    }

    fun getIntersection(other: NormalLine): Point? {
        val thisIsVertical = startX == endX
        val otherIsVertical = other.startX == other.endX
        val result: Point
        if (thisIsVertical || otherIsVertical) {
            if (thisIsVertical && otherIsVertical) {
                if (startX != other.startX)
                    return null
                // same x
                val thisMin = Math.min(startY, endY)
                val thisMax = Math.max(startY, endY)
                val otherMin = Math.min(other.startY, other.endY)
                val otherMax = Math.max(other.startY, other.endY)
                // check if y segments intersect
                return if (thisMin <= otherMax && thisMax >= otherMin) Point(startX, thisMin) else null
            }

            // only one is vertical
            val vertX: Double
            val slope: Double
            val intercept: Double
            if (thisIsVertical) {
                vertX = startX
                slope = other.slope
                intercept = other.intercept
            } else {
                vertX = other.startX
                slope = this.slope
                intercept = this.intercept
            }

            val y = slope * vertX + intercept

            result = Point(vertX, y)
        } else {
            val slopeDiff = slope - other.slope
            if (slopeDiff == 0.0)
                return if (other.intercept == intercept) Point(startX, startY) else null

            val x = (other.intercept - intercept) / slopeDiff
            val y = slope * x + intercept
            result = Point(x, y)
        }

        // TODO: check whether belongs
        return result
    }

    override fun toString(): String {
        return "NormalLine(startX=$startX, startY=$startY, endX=$endX, endY=$endY, normal=$normal)"
    }

    enum class Side {
        BACK, COLLINEAR, FRONT
    }
}
