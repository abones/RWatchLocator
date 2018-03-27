package com.rubius.rwatchlocator

import com.snatik.polygon.Point

/**
 *
 */
data class NormalLine(val startX: Double, val startY: Double, val endX: Double, val endY: Double, val room: Room? = null) {

    private val lengthX = endX - startX
    private val lengthY = endY - startY
    val length = Math.sqrt(lengthX * lengthX + lengthY * lengthY)
    val normal = Vector(-(endY - startY), endX - startX).normalize()

    private val slope = (endY - startY) / (endX - startX)
    private val intercept = startY - slope * startX

    val minX = Math.min(startX, endX)
    val maxX = Math.max(startX, endX)
    val minY = Math.min(startY, endY)
    val maxY = Math.max(startY, endY)

    fun getSide(x: Double, y: Double): Side {
        val dot = normal.dot(x - startX, y - startY)
        return when {
            dot < -Constants.PRECISION -> Side.BACK
            dot > Constants.PRECISION -> Side.FRONT
            else -> Side.COLLINEAR
        }
    }

    private fun segmentContains(point: Point): Boolean { // point is guaranteed to belong to line
        return point.x in minX..maxX && point.y in minY..maxY
    }

    fun getIntersection(other: NormalLine, shouldCheckSegment: Boolean = true): Point? {
        val thisIsVertical = startX == endX
        val otherIsVertical = other.startX == other.endX
        val result: Point
        if (thisIsVertical || otherIsVertical) {
            if (thisIsVertical && otherIsVertical) {
                if (startX != other.startX)
                    return null
                // same x, check if y segments intersect
                return if (!shouldCheckSegment || (minY <= other.maxY && maxY >= other.minY)) Point(startX, minY) else null
            }

            // only one is vertical
            val verticalX: Double
            val slope: Double
            val intercept: Double
            if (thisIsVertical) {
                verticalX = startX
                slope = other.slope
                intercept = other.intercept
            } else {
                verticalX = other.startX
                slope = this.slope
                intercept = this.intercept
            }

            val y = slope * verticalX + intercept

            result = Point(verticalX, y)
        } else {
            val slopeDiff = slope - other.slope
            if (slopeDiff == 0.0)
                return if (other.intercept == intercept) Point(startX, startY) else null

            val x = (other.intercept - intercept) / slopeDiff
            val y = slope * x + intercept
            result = Point(x, y)
        }

        return if (!shouldCheckSegment || (segmentContains(result) && other.segmentContains(result))) result else null
    }

    fun areSameLine(other: NormalLine): Boolean {
        return intercept == other.intercept && ((startX == endX && other.startX == other.endX) || (slope == other.slope))
    }

    override fun toString(): String {
        return "NormalLine(startX=$startX, startY=$startY, endX=$endX, endY=$endY, normal=$normal)"
    }

    enum class Side {
        BACK, COLLINEAR, FRONT
    }
}
