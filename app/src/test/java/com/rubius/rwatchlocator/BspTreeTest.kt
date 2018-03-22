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
        val result = BspTree.generateBsp(listOf(
            NormalLine(0.0, 0.0, 0.0, 1.0)
        ))

        assertLeaf(result!!)
        Assert.assertEquals(1, result.lines.size)
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
            NormalLine(0.0, 0.0, 1.0, 0.0), // this one will become root
            NormalLine(0.0, 1.0, 1.0, 1.0)
        ))

        Assert.assertNotNull(result!!.front)
        Assert.assertNull(result.back)

        assertLeaf(result.front!!)
    }

    @Test
    fun oneBehindAnother() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(0.0, 1.0, 1.0, 1.0), // this one will become root
            NormalLine(0.0, 0.0, 1.0, 0.0)
        ))

        Assert.assertNull(result!!.front)
        Assert.assertNotNull(result.back)

        assertLeaf(result.back!!)
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
}
