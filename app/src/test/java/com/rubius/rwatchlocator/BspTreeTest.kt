package com.rubius.rwatchlocator

import com.snatik.polygon.Point
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
    fun oneBehindAnother() {
        val result = BspTree.generateBsp(listOf(
            NormalLine(0.0, 1.0, 1.0, 1.0),
            NormalLine(0.0, 0.0, 1.0, 0.0)
        ))

        Assert.assertNull(result!!.front)
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

    @Test
    fun correctConvexForSplit() {
        val lines = listOf(
            NormalLine(4.0, 7.0, 10.0, 7.0),
            NormalLine(10.0, 7.0, 10.0, 11.0),
            NormalLine(10.0, 11.0, 8.0, 11.0),
            NormalLine(8.0, 11.0, 8.0, 14.0),
            NormalLine(8.0, 14.0, 4.0, 14.0),
            NormalLine(4.0, 14.0, 4.0, 7.0)
        )

        val result = BspTree.generateBsp(lines)

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

        Assert.assertFalse(isConvex)
    }

    @Test
    fun parallelAreConvex2() {
        val lines = listOf(
            // room 303
            NormalLine(0.0, 1.0, 1.0, 1.0),
            NormalLine(0.0, 0.0, 1.0, 0.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertFalse(isConvex)
    }

    @Test
    fun rightWoundNotConvex() {
        val lines = listOf(
            NormalLine(0.0, 0.0, 0.0, 1.0),
            NormalLine(0.0, 1.0, 1.0, 1.0),
            NormalLine(1.0, 0.0, 0.0, 0.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertFalse(isConvex)
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
    fun leftWoundIsConvex() {
        val lines = listOf(
            NormalLine(1.0, 1.0, 0.0, 0.0),
            NormalLine(0.0, 0.0, 1.0, 0.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertTrue(isConvex)
    }

    @Test
    fun leftWound2IsConvex() {
        val lines = listOf(
            NormalLine(1.0, 3.0, 1.0, 1.0),
            NormalLine(1.0, 1.0, 2.0, 1.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertTrue(isConvex)
    }

    @Test
    fun partOfLeftWoundSquareIsConvex() {
        val line1 = NormalLine(0.0, 0.0, 2.0, 0.0)
        val line2 = NormalLine(2.0, 0.0, 2.0, 2.0)
        val line3 = NormalLine(2.0, 2.0, 0.0, 2.0)
        val line4 = NormalLine(0.0, 2.0, 0.0, 0.0)

        val linesFull = listOf(line1, line2, line3, line4)

        for (startIndex in 0 until linesFull.size) {
            for (endIndex in startIndex + 1..linesFull.size) {
                val sublist = linesFull.subList(startIndex, endIndex)
                val isConvex = BspTree.isConvex(sublist)
                Assert.assertTrue("Sublist $sublist failed", isConvex)
            }
        }

        val isConvexSkip1 = BspTree.isConvex(listOf(line1, line3))
        Assert.assertTrue(isConvexSkip1)

        //val isConvexSkip2 = BspTree.isConvex(listOf(line1, line4)) // won't work, this is already larger than pi
        //Assert.assertTrue(isConvexSkip2)
    }

    @Test
    fun correct2ConvexSplit() {
        val lines = listOf(
            // room 1
            NormalLine(0.0, 0.0, 2.0, 0.0),
            NormalLine(2.0, 0.0, 2.0, 2.0),
            NormalLine(2.0, 2.0, 0.0, 2.0),
            NormalLine(0.0, 2.0, 0.0, 0.0),

            // room 2
            NormalLine(2.0, 0.0, 3.0, 0.0),
            NormalLine(3.0, 0.0, 3.0, 1.0),
            NormalLine(3.0, 1.0, 2.0, 1.0),
            NormalLine(2.0, 1.0, 2.0, 0.0)
        )

        val result = BspTree.generateBsp(lines)

        Assert.assertEquals(1, result!!.lines.size)
        Assert.assertEquals(0, result.convexLines.size)

        val line = result.lines[0]
        Assert.assertEquals(2.0, line.startX)
        Assert.assertEquals(0.0, line.startY)
        Assert.assertEquals(2.0, line.endX)
        Assert.assertEquals(2.0, line.endY)

        assertLeaf(result.front!!)
        assertLeaf(result.back!!)

        Assert.assertEquals(3, result.front!!.convexLines.size)
        Assert.assertEquals(3, result.back!!.convexLines.size)
    }

    @Test
    fun correctSelfIntersectSplit() {
        val lines = listOf(
            NormalLine(6.0, 5.0, 9.0, 1.0),
            NormalLine(9.0, 1.0, 4.0, 6.0),
            NormalLine(4.0, 6.0, 1.0, 6.0),
            NormalLine(1.0, 6.0, 6.0, 5.0)
        )

        val result = BspTree.generateBsp(lines)

        val line = result!!.lines[0]
        Assert.assertEquals(9.0, line.startX)
        Assert.assertEquals(1.0, line.startY)
        Assert.assertEquals(4.0, line.endX)
        Assert.assertEquals(6.0, line.endY)
    }

    @Test
    fun splitRoomInheritsItsParent() {
        val rooms = listOf(
            Room(
                "301",
                listOf(
                    Point(0.0, 7.0),
                    Point(4.0, 7.0),
                    Point(4.0, 14.0),
                    Point(0.0, 14.0)
                )
            ),
            Room(
                "302",
                listOf(
                    Point(0.0, 0.0),
                    Point(8.0, 0.0),
                    Point(8.0, 7.0),
                    Point(0.0, 7.0)
                )
            ),
            Room(
                "302a",
                listOf(
                    Point(8.0, 0.0),
                    Point(10.0, 0.0),
                    Point(10.0, 5.0),
                    Point(8.0, 5.0)
                )
            )
        )

        val lines = rooms.flatMap { it.lines }

        val result = BspTree.generateBsp(lines)

        val problematicNode = result!!.back!!.front!!

        Assert.assertEquals(2, problematicNode.convexLines.size)
        Assert.assertNotNull(problematicNode.frontRoom)
        Assert.assertSame(problematicNode.frontRoom, rooms[1])
    }

    @Test
    fun roomsDoNotExtendAboveAndBelow() {
        val rooms = listOf(
            Room(
                "302a",
                listOf(
                    Point(8.0, 0.0),
                    Point(10.0, 0.0),
                    Point(10.0, 5.0),
                    Point(8.0, 5.0)
                )
            ),
            Room(
                "302b",
                listOf(
                    Point(8.0, 5.0),
                    Point(10.0, 5.0),
                    Point(10.0, 7.0),
                    Point(8.0, 7.0)
                )
            )
        )

        val lines = rooms.flatMap { it.lines }

        val result = BspTree.generateBsp(lines)

        val leaf = result!!.getLeaf(Vector(9.0, 8.0))

        Assert.assertNull(leaf)
    }

    @Test
    fun keepsSplitDirectionWhenOptimizing() {
        val rooms = listOf(
            Room(
                "302a",
                listOf(
                    Point(8.0, 0.0),
                    Point(10.0, 0.0),
                    Point(10.0, 5.0),
                    Point(8.0, 5.0)
                )
            ),
            Room(
                "302b",
                listOf(
                    Point(8.0, 5.0),
                    Point(10.0, 5.0),
                    Point(10.0, 7.0),
                    Point(8.0, 7.0)
                )
            )
        )

        val lines = rooms.flatMap { it.lines }

        val result = BspTree.generateBsp(lines)

        val line = result!!.lines[0]
        Assert.assertEquals(10.0, line.startX)
        Assert.assertEquals(5.0, line.startY)
        Assert.assertEquals(8.0, line.endX)
        Assert.assertEquals(5.0, line.endY)

        Assert.assertEquals("302a", rooms[0].name)
        Assert.assertSame(rooms[0], result.front!!.frontRoom)
        Assert.assertEquals("302b", rooms[1].name)
        Assert.assertSame(rooms[1], result.back!!.frontRoom)
    }

    @Test
    fun getsRightRoomCorrectly() {
        val rooms = listOf(
            Room(
                "302b",
                listOf(
                    Point(8.0, 5.0),
                    Point(10.0, 5.0),
                    Point(10.0, 7.0),
                    Point(8.0, 7.0)
                )
            ),
            Room(
                "303",
                listOf(
                    Point(10.0, 0.0),
                    Point(18.0, 0.0),
                    Point(18.0, 7.0),
                    Point(10.0, 7.0)
                )
            )
        )

        val lines = rooms.flatMap { it.lines }

        val result = BspTree.generateBsp(lines)

        val leftPoint = Vector(9.0, 6.0)
        val leftLeaf = result!!.getLeaf(leftPoint)

        val rightPoint = Vector(11.0, 5.0)
        val rightLeaf = result.getLeaf(rightPoint)

        Assert.assertEquals("302b", rooms[0].name)
        Assert.assertSame(rooms[0], leftLeaf!!.frontRoom)
        Assert.assertEquals("303", rooms[1].name)
        Assert.assertSame(rooms[1], rightLeaf!!.frontRoom)
    }

    @Test
    fun doesNotLoseSplitLines() {
        val rooms = listOf(
            Room(
                "303",
                listOf(
                    Point(10.0, 0.0),
                    Point(18.0, 0.0),
                    Point(18.0, 7.0),
                    Point(10.0, 7.0)
                )
            ),
            Room(
                "H2",
                listOf(
                    Point(10.0, 7.0),
                    Point(45.0, 7.0),
                    Point(45.0, 11.0),
                    Point(10.0, 11.0)
                )
            )
        )

        val lines = rooms.flatMap { it.lines }

        val result = BspTree.generateBsp(lines)

        Assert.assertEquals(1, result!!.lines.size)
        val line = result.lines[0]
        Assert.assertEquals(45.0, line.startX)
        Assert.assertEquals(10.0, line.endX)
    }

    private fun assertNode(node: TreeNode, lineCount: Int, convexLineCount: Int, startX: Double, startY: Double, endX: Double, endY: Double) {
        Assert.assertEquals(lineCount, node.lines.size)
        Assert.assertEquals(convexLineCount, node.convexLines.size)
        val curLine = node.lines[0]
        Assert.assertEquals(startX, curLine.startX)
        Assert.assertEquals(startY, curLine.startY)
        Assert.assertEquals(endX, curLine.endX)
        Assert.assertEquals(endY, curLine.endY)
    }

    @Test
    fun noConvexLinesFromWrongNodes() {
        val rooms = listOf(
            Room(
                "301",
                listOf(
                    Point(0.0, 7.0),
                    Point(4.0, 7.0),
                    Point(4.0, 14.0),
                    Point(0.0, 14.0)
                )
            ),
            Room(
                "302a",
                listOf(
                    Point(8.0, 0.0),
                    Point(10.0, 0.0),
                    Point(10.0, 5.0),
                    Point(8.0, 5.0)
                )
            ),
            Room(
                "302b",
                listOf(
                    Point(8.0, 5.0),
                    Point(10.0, 5.0),
                    Point(10.0, 7.0),
                    Point(8.0, 7.0)
                )
            ),
            Room(
                "303",
                listOf(
                    Point(10.0, 0.0),
                    Point(18.0, 0.0),
                    Point(18.0, 7.0),
                    Point(10.0, 7.0)
                )
            ),
            Room(
                "304a",
                listOf(
                    Point(18.0, 0.0),
                    Point(22.0, 0.0),
                    Point(22.0, 7.0),
                    Point(18.0, 7.0)
                )
            ),
            Room(
                "304",
                listOf(
                    Point(22.0, 0.0),
                    Point(26.0, 0.0),
                    Point(26.0, 7.0),
                    Point(22.0, 7.0)
                )
            ),
            Room(
                "305",
                listOf(
                    Point(26.0, 0.0),
                    Point(30.0, 0.0),
                    Point(30.0, 7.0),
                    Point(26.0, 7.0)
                )
            )
        )

        val lines = rooms.flatMap { it.lines }

        val result = BspTree.generateBsp(lines)

        var curNode = result!!
        assertNode(curNode, 1, 0, 18.0, 0.0, 18.0, 7.0)

        curNode = curNode.front!!
        assertNode(curNode, 1, 0, 10.0, 5.0, 8.0, 5.0)

        curNode = curNode.back!!
        assertNode(curNode, 2, 0, 0.0, 7.0, 4.0, 7.0)

        curNode = curNode.back!!
        assertNode(curNode, 1, 0, 10.0, 5.0, 10.0, 7.0)
    }

    @Test
    fun threeParallelLinesAreNotConvex() {
        val lines = listOf(
            // room 303
            NormalLine(10.0, 5.0, 10.0, 7.0),
            NormalLine(8.0, 7.0, 8.0, 5.0),
            NormalLine(10.0, 7.0, 10.0, 5.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertFalse(isConvex)
    }

    @Test
    fun threeParallelLinesAreNotConvex2() {
        val lines = listOf(
            NormalLine(10.0, 14.0, 8.0, 14.0),
            NormalLine(8.0, 14.0, 10.0, 14.0),
            NormalLine(10.0, 18.0, 8.0, 18.0)
        )

        val isConvex = BspTree.isConvex(lines)

        Assert.assertFalse(isConvex)
    }
}
