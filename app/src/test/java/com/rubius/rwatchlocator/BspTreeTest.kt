package com.rubius.rwatchlocator

import junit.framework.Assert
import org.junit.Test

/**
 *
 */
class BspTreeTest {
    private fun assertLeaf(result: TreeNode) {
        Assert.assertNull(result.front)
        Assert.assertNull(result.back)
        Assert.assertEquals(0, result.pendingFront.size)
        Assert.assertEquals(0, result.pendingBack.size)
    }

    @Test
    fun emptyListProducesNull() {
        val result = BspTree.generateBsp(listOf())

        Assert.assertNull(result)
    }

    @Test
    fun singleLineProducesSimpleNode() {
        val lines = listOf(
            NormalLine(0.0, 0.0, 0.0, 1.0)
        )

        val result = BspTree.generateBsp(lines)

        assertLeaf(result!!)
        Assert.assertEquals(1, result.convexLines.size)
    }

    /*@Test
    fun joinsAdjacentLines() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(0.0, 2.0, 0.0, 3.0),
            NormalLine(0.0, 0.0, 0.0, 1.0),
            NormalLine(1.0, 0.0, 0.0, 2.0)
        ))

        assertLeaf(result!!)
        Assert.assertEquals(1, result.lines.size)
        Assert.assertEquals(0.0, result.lines[0].startX)
        Assert.assertEquals(0.0, result.lines[0].startY)
        Assert.assertEquals(0.0, result.lines[0].endX)
        Assert.assertEquals(3.0, result.lines[0].endY)
    }*/

    @Test
    fun convexBelongInOneNode() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(1.0, 1.0, 0.0, 1.0), // \/ facing each other
            NormalLine(0.0, 1.0, 0.0, 0.0), //
            NormalLine(0.0, 0.0, 1.0, 0.0)  // /\
        ))

        assertLeaf(result!!)
        Assert.assertEquals(3, result.convexLines.size)
    }

    @Test
    fun triangleBelongsInOneNode() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(0.0, 0.0, 1.0, 0.0),
            NormalLine(1.0, 0.0, 0.0, 1.0),
            NormalLine(0.0, 1.0, 0.0, 0.0)
        ))

        assertLeaf(result!!)
        Assert.assertEquals(3, result.convexLines.size)
    }

    @Test
    fun oneInFrontOfOther() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(0.0, 0.0, 1.0, 0.0),
            NormalLine(0.0, 1.0, 1.0, 1.0)
        ))

        assertLeaf(result!!)
        Assert.assertEquals(2, result.convexLines.size)
    }

    @Test
    fun oneBehindAnother() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(0.0, 1.0, 1.0, 1.0),
            NormalLine(0.0, 0.0, 1.0, 0.0)
        ))

        assertLeaf(result!!)
        Assert.assertEquals(2, result.convexLines.size)
    }

    @Test
    fun picksMostBalancedSplit() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(0.0, -1.0, 0.0, 1.0),
            NormalLine(1.0, -1.0, 1.0, 1.0), // < this one
            NormalLine(2.0, -1.0, 2.0, 1.0),
            NormalLine(0.0, 0.0, 2.0, 0.0)
        ))

        Assert.assertEquals(1.0, result!!.lines[0].startX) // i.e. picks the line on X axis first
        Assert.assertEquals(-1.0, result.lines[0].startY)
        Assert.assertEquals(1.0, result.lines[0].endX)
        Assert.assertEquals(1.0, result.lines[0].endY)
    }

    @Test
    fun correctConvexForSplit() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(4.0, 7.0, 10.0, 7.0),
            NormalLine(10.0, 7.0, 10.0, 11.0),
            NormalLine(10.0, 11.0, 8.0, 11.0),
            NormalLine(8.0, 11.0, 8.0, 14.0),
            NormalLine(8.0, 14.0, 4.0, 14.0),
            NormalLine(4.0, 14.0, 4.0, 7.0)
        ))

        Assert.assertEquals(1, result!!.lines.size)
        Assert.assertEquals(0, result.front!!.lines.size)
        Assert.assertEquals(0, result.back!!.lines.size)
        Assert.assertEquals(3, result.front!!.convexLines.size)
        Assert.assertEquals(3, result.back!!.convexLines.size)
    }

    @Test
    fun doesNotJoinWrongLinesConvex() {
        val lines = listOf(
            // room 303
            NormalLine(10.0, 0.0, 18.0, 0.0),
            NormalLine(18.0, 0.0, 18.0, 7.0),
            NormalLine(18.0, 7.0, 10.0, 7.0),
            NormalLine(10.0, 7.0, 10.0, 0.0),
            // room 310
            NormalLine(14.0, 11.0, 22.0, 11.0),
            NormalLine(22.0, 11.0, 22.0, 14.0),
            NormalLine(22.0, 14.0, 18.0, 14.0),
            NormalLine(18.0, 14.0, 18.0, 18.0),
            NormalLine(18.0, 18.0, 14.0, 18.0),
            NormalLine(14.0, 18.0, 14.0, 11.0),
            // room 310a
            NormalLine(18.0, 14.0, 22.0, 14.0),
            NormalLine(22.0, 14.0, 22.0, 18.0),
            NormalLine(22.0, 18.0, 18.0, 18.0),
            NormalLine(18.0, 18.0, 18.0, 14.0)
        )

        val result = BspTree.generateBsp(lines)

        Assert.assertEquals(2, result!!.lines.size)
    }

    @Test
    fun doesNotSplitConvex() {
        val lines = listOf(
            // room 303
            NormalLine(10.0, 0.0, 18.0, 0.0),
            NormalLine(18.0, 0.0, 18.0, 7.0),
            NormalLine(18.0, 7.0, 10.0, 7.0),
            NormalLine(10.0, 7.0, 10.0, 0.0),
            // room 310
            NormalLine(14.0, 11.0, 22.0, 11.0),
            NormalLine(22.0, 11.0, 22.0, 14.0),
            NormalLine(22.0, 14.0, 18.0, 14.0),
            NormalLine(18.0, 14.0, 18.0, 18.0),
            NormalLine(18.0, 18.0, 14.0, 18.0),
            NormalLine(14.0, 18.0, 14.0, 11.0)
        )

        val result = BspTree.generateBsp(lines)

        assertLeaf(result!!.back!!)

        val frontFront = result.front!!.front
        val frontBack = result.front!!.front
        assertLeaf(frontBack!!)
        assertLeaf(frontFront!!)
        Assert.assertEquals(2, frontFront.convexLines.size)
        Assert.assertEquals(2, frontBack.convexLines.size)
    }
/*
    @Test
    fun doesNotSplitConvex() {
        val lines = listOf(
            // room 310
            NormalLine(14.0, 11.0, 22.0, 11.0),
            NormalLine(22.0, 11.0, 22.0, 14.0),
            NormalLine(22.0, 14.0, 18.0, 14.0),
            NormalLine(18.0, 14.0, 18.0, 18.0),
            NormalLine(18.0, 18.0, 14.0, 18.0),
            NormalLine(14.0, 18.0, 14.0, 11.0),
            // room 310a
            NormalLine(18.0, 14.0, 22.0, 14.0),
            NormalLine(22.0, 14.0, 22.0, 18.0),
            NormalLine(22.0, 18.0, 18.0, 18.0),
            NormalLine(18.0, 18.0, 18.0, 14.0)
        )

        val result = BspTree.generateBsp(lines)

        Assert.assertEquals(1, result!!.lines.size)
        assertLeaf(result.back!!)
        Assert.assertEquals(0, result.back!!.lines.size)
        Assert.assertEquals(4, result.back!!.convexLines.size)

        val frontSplit = result.front!!

        Assert.assertEquals(1, frontSplit.lines.size)
        Assert.assertEquals(0, frontSplit.convexLines.size)
        assertLeaf(frontSplit.front!!)
        assertLeaf(frontSplit.back!!)
        Assert.assertEquals(2, frontSplit.front!!.convexLines.size)
        Assert.assertEquals(2, frontSplit.back!!.convexLines.size)
    }*/

    @Test
    fun parallelAreConvex1() {
        val lines = listOf(
            // room 303
            NormalLine(0.0, 1.0, 1.0, 1.0),
            NormalLine(1.0, 0.0, 0.0, 0.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertTrue(isConvex)
    }

    @Test
    fun parallelAreConvex2() {
        val lines = listOf(
            // room 303
            NormalLine(0.0, 1.0, 1.0, 1.0),
            NormalLine(0.0, 0.0, 1.0, 0.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertTrue(isConvex)
    }

    @Test
    fun leftConvex() {
        val lines = listOf(
            NormalLine(0.0, 0.0, 0.0, 1.0),
            NormalLine(0.0, 1.0, 1.0, 1.0),
            NormalLine(1.0, 0.0, 0.0, 0.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertTrue(isConvex)
    }

    @Test
    fun obtuseNotConvex() {
        val lines = listOf(
            NormalLine(0.0, 7.0, 6.0, 6.0),
            NormalLine(6.0, 6.0, 1.0, 1.0)
            //NormalLine(4.0, 9.0, 0.0, 7.0),
            //NormalLine(0.0, 7.0, 6.0, 6.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertFalse(isConvex)
    }

    @Test
    fun obtuse2NotConvex() {
        val lines = listOf(
            //NormalLine(10.0, 10.0, 0.0, 5.0),
            //NormalLine(0.0, 5.0, 10.0, 0.0)
            NormalLine(1.0, 1.0, 0.0, 0.0),
            NormalLine(0.0, 0.0, 1.0, 0.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertFalse(isConvex)
    }
}
