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

    @Test
    fun identicalLinesIntersectAtStart() {
        val line1 = NormalLine(0.0, 0.0, 1.0, 0.0)
        val line2 = NormalLine(0.0, 0.0, 1.0, 0.0)

        val point = line1.getIntersection(line2)

        Assert.assertNotNull(point)
        Assert.assertEquals(0.0, point!!.x)
        Assert.assertEquals(0.0, point.y)
    }

    @Test
    fun collinearLinesIntersectAtStart() {
        val line1 = NormalLine(0.0, 0.0, 1.0, 0.0)
        val line2 = NormalLine(0.0, 0.0, 2.0, 0.0)

        val point = line1.getIntersection(line2)

        Assert.assertNotNull(point)
        Assert.assertEquals(0.0, point!!.x)
        Assert.assertEquals(0.0, point.y)
    }

    @Test
    fun parallelLinesDoNotIntersect() {
        val line1 = NormalLine(0.0, 0.0, 1.0, 0.0)
        val line2 = NormalLine(0.0, 1.0, 1.0, 1.0)

        val point = line1.getIntersection(line2)

        Assert.assertNull(point)
    }

    @Test
    fun linesIntersectAtCommonPoint() {
        val line1 = NormalLine(0.0, 0.0, 1.0, 0.0)
        val line2 = NormalLine(1.0, 0.0, 1.0, 1.0)

        val point = line1.getIntersection(line2)

        Assert.assertNotNull(point)
        Assert.assertEquals(1.0, point!!.x)
        Assert.assertEquals(0.0, point.y)
    }

    @Test
    fun linesIntersect() {
        val line1 = NormalLine(0.0, 0.0, 2.0, 0.0)
        val line2 = NormalLine(1.0, 1.0, 1.0, -1.0)

        val point = line1.getIntersection(line2)

        Assert.assertNotNull(point)
        Assert.assertEquals(1.0, point!!.x)
        Assert.assertEquals(0.0, point.y)
    }

    @Test
    fun segmentsDoNotIntersect() {
        val line1 = NormalLine(0.0, 0.0, 2.0, 0.0)
        val line2 = NormalLine(1.0, 2.0, 1.0, 1.0) // lines intersect, segments do not

        val point = line1.getIntersection(line2)

        Assert.assertNull(point)
    }

    @Test
    fun parallelVerticalsDoNotIntersect() {
        val line1 = NormalLine(0.0, 0.0, 0.0, 1.0)
        val line2 = NormalLine(1.0, 0.0, 1.0, 1.0)

        val point = line1.getIntersection(line2)

        Assert.assertNull(point)
    }

    @Test
    fun parallelVerticalsIntersect() {
        val line1 = NormalLine(0.0, 0.0, 0.0, 1.0)
        val line2 = NormalLine(0.0, -1.0, 0.0, 0.0)

        val point = line1.getIntersection(line2)

        Assert.assertNotNull(point)
        Assert.assertEquals(0.0, point!!.x)
        Assert.assertEquals(0.0, point.y)
    }

    @Test
    fun parallelVerticalSegmentsDoNotIntersect() {
        val line1 = NormalLine(0.0, 0.0, 0.0, 1.0)
        val line2 = NormalLine(0.0, 2.0, 0.0, 3.0)

        val point = line1.getIntersection(line2)

        Assert.assertNull(point)
    }

    @Test
    fun firstVerticalIntersects() {
        val line1 = NormalLine(2.0, 0.0, 2.0, 4.0)
        val line2 = NormalLine(0.0, 0.0, 4.0, 4.0) // lines intersect, segments do not

        val point = line1.getIntersection(line2)

        Assert.assertNotNull(point)
        Assert.assertEquals(2.0, point!!.x)
        Assert.assertEquals(2.0, point.y)
    }
}
