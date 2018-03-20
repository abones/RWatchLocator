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
}
