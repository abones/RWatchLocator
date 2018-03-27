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
        val line1 = NormalLine(quadrant1Line.start.x, quadrant1Line.start.y, quadrant1Line.end.x, quadrant1Line.start.y)
        val line2 = NormalLine(quadrant2Line.start.x, quadrant2Line.start.y, quadrant2Line.end.x, quadrant2Line.start.y)
        val line3 = NormalLine(quadrant3Line.start.x, quadrant3Line.start.y, quadrant3Line.end.x, quadrant3Line.start.y)
        val line4 = NormalLine(quadrant4Line.start.x, quadrant4Line.start.y, quadrant4Line.end.x, quadrant4Line.start.y)

        Assert.assertEquals(1.0, line1.normal.length, Constants.PRECISION)
        Assert.assertEquals(1.0, line2.normal.length, Constants.PRECISION)
        Assert.assertEquals(1.0, line3.normal.length, Constants.PRECISION)
        Assert.assertEquals(1.0, line4.normal.length, Constants.PRECISION)
    }

    @Test
    fun lineHasCorrectNormal() {
        val line1 = NormalLine(quadrant1Line.start.x, quadrant1Line.start.y, quadrant1Line.end.x, quadrant1Line.end.y)
        val line2 = NormalLine(quadrant2Line.start.x, quadrant2Line.start.y, quadrant2Line.end.x, quadrant2Line.end.y)
        val line3 = NormalLine(quadrant3Line.start.x, quadrant3Line.start.y, quadrant3Line.end.x, quadrant3Line.end.y)
        val line4 = NormalLine(quadrant4Line.start.x, quadrant4Line.start.y, quadrant4Line.end.x, quadrant4Line.end.y)

        Assert.assertEquals(-UNIT_VECTOR_COORDINATE, line1.normal.x, Constants.PRECISION)
        Assert.assertEquals(UNIT_VECTOR_COORDINATE, line1.normal.y, Constants.PRECISION)

        Assert.assertEquals(UNIT_VECTOR_COORDINATE, line2.normal.x, Constants.PRECISION)
        Assert.assertEquals(UNIT_VECTOR_COORDINATE, line2.normal.y, Constants.PRECISION)

        Assert.assertEquals(UNIT_VECTOR_COORDINATE, line3.normal.x, Constants.PRECISION)
        Assert.assertEquals(-UNIT_VECTOR_COORDINATE, line3.normal.y, Constants.PRECISION)

        Assert.assertEquals(-UNIT_VECTOR_COORDINATE, line4.normal.x, Constants.PRECISION)
        Assert.assertEquals(-UNIT_VECTOR_COORDINATE, line4.normal.y, Constants.PRECISION)
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

    @Test
    fun canCompareIntersectionPointDirectly() {
        val line1 = NormalLine(10.0, 11.0, 8.0, 11.0)
        val line2 = NormalLine(10.0, 7.0, 10.0, 11.0)

        val point = line1.getIntersection(line2, false)

        Assert.assertEquals(line2.endX, point!!.x)
        Assert.assertEquals(line2.endY, point.y)
    }

    @Test
    fun slopedSegmentsAreCollinear() {
        val line1 = NormalLine(1.0, 1.0, 2.0, 2.0)
        val line2 = NormalLine(2.0, 2.0, 1.0, 1.0)

        val sideStart = line1.getSide(line2.startX, line2.startY)
        val sideEnd = line1.getSide(line2.endX, line2.endY)

        Assert.assertEquals(NormalLine.Side.COLLINEAR, sideStart)
        Assert.assertEquals(NormalLine.Side.COLLINEAR, sideEnd)
    }

    @Test
    fun horizontalSegmentsAreCollinear() {
        val line1 = NormalLine(0.0, 18.0, 7.0, 18.0)
        val line2 = NormalLine(7.0, 18.0, 0.0, 18.0)

        val sideStart = line1.getSide(line2.startX, line2.startY)
        val sideEnd = line1.getSide(line2.endX, line2.endY)

        Assert.assertEquals(NormalLine.Side.COLLINEAR, sideStart)
        Assert.assertEquals(NormalLine.Side.COLLINEAR, sideEnd)
    }

    @Test
    fun verticalSegmentsAreCollinear() {
        val line1 = NormalLine(18.0, 0.0, 18.0, 7.0)
        val line2 = NormalLine(18.0, 7.0, 18.0, 0.0)

        val sideStart = line1.getSide(line2.startX, line2.startY)
        val sideEnd = line1.getSide(line2.endX, line2.endY)

        Assert.assertEquals(NormalLine.Side.COLLINEAR, sideStart)
        Assert.assertEquals(NormalLine.Side.COLLINEAR, sideEnd)
    }
}

