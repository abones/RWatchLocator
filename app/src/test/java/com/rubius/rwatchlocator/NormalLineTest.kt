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

    @Test
    fun zeroLineHasZeroNormal() {
        val line = NormalLine(zeroLine)

        Assert.assertEquals(0.0, line.normal.length)
    }

    @Test
    fun lineNormalizesVector() {
        val line1 = NormalLine(quadrant1Line)
        val line2 = NormalLine(quadrant2Line)
        val line3 = NormalLine(quadrant3Line)
        val line4 = NormalLine(quadrant4Line)

        Assert.assertEquals(1.0, line1.normal.length)
        Assert.assertEquals(1.0, line2.normal.length)
        Assert.assertEquals(1.0, line3.normal.length)
        Assert.assertEquals(1.0, line4.normal.length)
    }

    @Test
    fun lineHasCorrectNormal() {
        val line1 = NormalLine(quadrant1Line)
        val line2 = NormalLine(quadrant2Line)
        val line3 = NormalLine(quadrant3Line)
        val line4 = NormalLine(quadrant4Line)

        Assert.assertEquals(-1.0, line1.normal.x)
        Assert.assertEquals(1.0, line1.normal.y)

        Assert.assertEquals(1.0, line2.normal.x)
        Assert.assertEquals(1.0, line2.normal.y)

        Assert.assertEquals(1.0, line3.normal.x)
        Assert.assertEquals(-1.0, line3.normal.y)

        Assert.assertEquals(-1.0, line4.normal.x)
        Assert.assertEquals(-1.0, line4.normal.y)
    }
}
