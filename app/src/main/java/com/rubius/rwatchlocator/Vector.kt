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
        return x * other.x + y * other.y
    }

    fun dot(otherX: Double, otherY: Double): Double {
        return x * otherX + y * otherY
    }

    private fun crossSin(other: Vector): Double {
        return (x * other.y - other.x * y)/* / (length * other.length)*/
    }

    fun angleBetween(other: Vector): Double {
        return Math.atan2(crossSin(other), dot(other))
        //Math.atan2(y, x) - Math.atan2(other.y, other.x)
    }

    fun signedAngleBetween(other: Vector): Double {
        val result = angleBetween(other)
        return if (result > Math.PI) Math.PI - result else result
    }

    override fun toString(): String {
        return "Vector(x=$x, y=$y, length=$length)"
    }
}
