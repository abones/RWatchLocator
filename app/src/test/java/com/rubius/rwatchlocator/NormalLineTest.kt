package com.rubius.rwatchlocator

import com.snatik.polygon.Line
import com.snatik.polygon.Point
import junit.framework.Assert
import org.junit.Test

/**
 *
 */
class NormalLineTest {
    private val zeroLine = Line(Point(0.0, 0.0), Point(0.0, 0.0))
    private val quadrant1Line = Line(Point(2.0, 2.0), Point(4.0, 4.0))
    private val quadrant2Line = Line(Point(2.0, -2.0), Point(4.0, -4.0))
    private val quadrant3Line = Line(Point(-2.0, -2.0), Point(-4.0, -4.0))
    private val quadrant4Line = Line(Point(-2.0, 2.0), Point(-4.0, 4.0))

    private val UNIT_VECTOR_COORDINATE = Math.sqrt(0.5)

    @Test
    fun lineNormalizesVector() {
        val line1 = NormalLine(quadrant1Line)
        val line2 = NormalLine(quadrant2Line)
        val line3 = NormalLine(quadrant3Line)
        val line4 = NormalLine(quadrant4Line)

        Assert.assertEquals(1.0, line1.normal.length, 0.0001)
        Assert.assertEquals(1.0, line2.normal.length, 0.0001)
        Assert.assertEquals(1.0, line3.normal.length, 0.0001)
        Assert.assertEquals(1.0, line4.normal.length, 0.0001)
    }

    @Test
    fun lineHasCorrectNormal() {
        val line1 = NormalLine(quadrant1Line)
        val line2 = NormalLine(quadrant2Line)
        val line3 = NormalLine(quadrant3Line)
        val line4 = NormalLine(quadrant4Line)

        Assert.assertEquals(-UNIT_VECTOR_COORDINATE, line1.normal.x, 0.0001)
        Assert.assertEquals(UNIT_VECTOR_COORDINATE, line1.normal.y, 0.0001)

        Assert.assertEquals(UNIT_VECTOR_COORDINATE, line2.normal.x, 0.0001)
        Assert.assertEquals(UNIT_VECTOR_COORDINATE, line2.normal.y, 0.0001)

        Assert.assertEquals(UNIT_VECTOR_COORDINATE, line3.normal.x, 0.0001)
        Assert.assertEquals(-UNIT_VECTOR_COORDINATE, line3.normal.y, 0.0001)

        Assert.assertEquals(-UNIT_VECTOR_COORDINATE, line4.normal.x, 0.0001)
        Assert.assertEquals(-UNIT_VECTOR_COORDINATE, line4.normal.y, 0.0001)
    }
}
