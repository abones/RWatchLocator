package com.rubius.rwatchlocator

import junit.framework.Assert
import org.junit.Test

/**
 *
 */
class VectorTest {
    @Test
    fun zeroLengthForZeroPoint() {
        val vector = Vector(0.0, 0.0)

        Assert.assertEquals(0.0, vector.length, 0.0)
    }

    @Test
    fun unitLengthForUnitVectors() {
        val vector1 = Vector(0.0, 1.0)
        val vector2 = Vector(1.0, 0.0)

        Assert.assertEquals(1.0, vector1.length, 0.0)
        Assert.assertEquals(1.0, vector2.length, 0.0)
    }

    @Test
    fun calculatesCorrectLength() {
        val vector = Vector(2.0, 2.0)

        Assert.assertEquals(Math.sqrt(8.0), vector.length, 0.0)
    }

    @Test
    fun normalizedVectorHasUnitLength() {
        val vector = Vector(2.0, 2.0).normalize()

        Assert.assertEquals(1.0, vector.length, Constants.PRECISION)
    }

    @Test
    fun orthogonalVectorsHaveZeroDot() {
        val vector1 = Vector(1.0, 1.0)
        val vector2 = Vector(2.0, -2.0)

        val dot = vector1.dot(vector2)

        Assert.assertEquals(0.0, dot, 0.0)
    }

    @Test
    fun proportionalityViaNormalization() {
        val vector1 = Vector(2.0, 2.0).normalize()
        val vector2 = Vector(4.0, 4.0).normalize()

        val dot = vector1.dot(vector2)

        Assert.assertEquals(1.0, dot, Constants.PRECISION)
    }

    @Test
    fun dotIsCommutative() {
        val vector1 = Vector(2.0, 2.0)
        val vector2 = Vector(4.0, 4.0)

        val dot12 = vector1.dot(vector2)
        val dot21 = vector2.dot(vector1)

        Assert.assertEquals(dot12, dot21, 0.0)
    }

    @Test
    fun zeroAngle() {
        val v1 = Vector(1.0, 1.0)
        val v2 = Vector(1.0, 1.0)

        val angle1 = v1.signedAngleBetween(v2)
        val angle2 = v2.signedAngleBetween(v1)

        Assert.assertEquals(0.0, angle1)
        Assert.assertEquals(0.0, angle2)
    }

    @Test
    fun pi4Angle() {
        val v1 = Vector(1.0, 1.0)
        val v2 = Vector(0.0, 1.0)

        val angle1 = v1.signedAngleBetween(v2)
        val angle2 = v2.signedAngleBetween(v1)

        Assert.assertEquals(Math.PI / 4.0, angle1)
        Assert.assertEquals(-Math.PI / 4.0, angle2)
    }

    @Test
    fun pi2Angle() {
        val v1 = Vector(1.0, 1.0)
        val v2 = Vector(-1.0, 1.0)

        val angle1 = v1.signedAngleBetween(v2)
        val angle2 = v2.signedAngleBetween(v1)

        Assert.assertEquals(Math.PI / 2.0, angle1)
        Assert.assertEquals(-Math.PI / 2.0, angle2)
    }

    @Test
    fun piAngle() {
        val v1 = Vector(1.0, 1.0)
        val v2 = Vector(-1.0, -1.0)

        val angle1 = v1.signedAngleBetween(v2)
        val angle2 = v2.signedAngleBetween(v1)

        Assert.assertEquals(Math.PI, angle1)
        Assert.assertEquals(Math.PI, angle2) // both positive
    }
}
