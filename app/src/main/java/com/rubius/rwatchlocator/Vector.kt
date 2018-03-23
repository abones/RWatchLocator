package com.rubius.rwatchlocator

/**
 *
 */
class Vector(val x: Double, val y: Double) {
    val length = Math.sqrt(x * x + y * y)

    fun normalize(): Vector {
        return Vector(x / length, y / length)
    }

    fun dot(other: Vector): Double {
        return x / length * other.x / other.length + y / length * other.y / other.length
    }

    fun dot(otherX: Double, otherY: Double): Double {
        return x * otherX + y * otherY
    }

    fun crossSin(other: Vector): Double {
        return (x * other.y - other.x * y)/* / (length * other.length)*/
    }

    fun angleBetween(other: Vector): Double {
        //return Math.atan2(crossSin(other), dot(other))
        //return -(Math.atan2(y, x) - Math.atan2(other.y, other.x))
        /*val x1 = x
        val x2 = other.x
        val y1 = y
        val y2 = other.y
        val dot = x1 * x2 + y1 * y2
        val det =x1 * y2 - y1 * x2
        return Math.atan2(det, dot)*/
        //return Math.PI + signedAngleBetween(other)
        return Math.PI + signedAngleBetween(other)
    }

    fun signedAngleBetween(other: Vector): Double {
        //return if (result > Math.PI) Math.PI - result else result
        val  angle = Math.acos(dot(other))
        return if (crossSin(other) < 0) -angle else angle
    }

    override fun toString(): String {
        return "Vector(x=$x, y=$y, length=$length)"
    }
}
