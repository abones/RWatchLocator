package com.rubius.rwatchlocator

/**
 *
 */
class BspTree {
    companion object {
        private fun getSplittingLine(lines: List<NormalLine>): TreeNode? {
            var minSum: Int? = null
            var bestSplit: TreeNode? = null
            for (potentialSplitLine in lines) {
                val potentialSplit = TreeNode()
                potentialSplit.lines.add(potentialSplitLine)

                var splitCount = 0

                for (currentLine in lines) {
                    if (currentLine === potentialSplitLine)
                        continue

                    val sideStart = potentialSplitLine.getSide(currentLine.startX, currentLine.startY)
                    val sideEnd = potentialSplitLine.getSide(currentLine.endX, currentLine.endY)

                    if (sideStart == sideEnd)
                        placeLine(currentLine, potentialSplit, sideStart)
                    else {
                        val point = potentialSplitLine.getIntersection(currentLine, false)
                        if (point != null) {
                            val pointAtLineStart = sideStart == NormalLine.Side.COLLINEAR
                            val pointAtLineEnd = sideEnd == NormalLine.Side.COLLINEAR

                            when {
                                pointAtLineStart -> placeLine(currentLine, potentialSplit, sideEnd)
                                pointAtLineEnd -> placeLine(currentLine, potentialSplit, sideStart)
                                else -> {
                                    val lineStart = NormalLine(currentLine.startX, currentLine.startY, point.x, point.y)
                                    val lineEnd = NormalLine(point.x, point.y, currentLine.endX, currentLine.endY)
                                    placeLine(lineStart, potentialSplit, sideStart)
                                    placeLine(lineEnd, potentialSplit, sideEnd)
                                    ++splitCount
                                }
                            }
                        }
                    }
                }

                val sum = Math.abs(potentialSplit.pendingFront.size - potentialSplit.pendingBack.size) + splitCount

                if (sum == 0)
                    return potentialSplit // no point in looking further
                if (minSum == null || sum < minSum) {
                    minSum = sum
                    bestSplit = potentialSplit
                }
            }

            return bestSplit
        }

        private fun placeLine(secondLine: NormalLine, currentSplit: TreeNode, side: NormalLine.Side) {
            when (side) {
                NormalLine.Side.FRONT -> currentSplit.pendingFront.add(secondLine)
                NormalLine.Side.BACK -> currentSplit.pendingBack.add(secondLine)
                NormalLine.Side.COLLINEAR -> currentSplit.lines.add(secondLine)
            }
        }

        fun generateBsp(lines: List<NormalLine>): TreeNode? {
            if (isConvex(lines)) {
                val result = TreeNode()
                result.room = lines[0].room
                result.convexLines.addAll(lines)
                return result
            }

            val split = getSplittingLine(lines) ?: return null

            optimizeLines(split)

            if (split.pendingFront.size > 0) {
                split.front = generateBsp(split.pendingFront)
                split.pendingFront.clear()
            }

            if (split.pendingBack.size > 0) {
                split.back = generateBsp(split.pendingBack)
                split.pendingBack.clear()
            }

            return split
        }

        private fun optimizeLines(split: TreeNode) {
            if (split.lines.size <= 1)
                return
            split.lines.sortWith(compareBy({ it.minX }, { it.minY }))

            val result = arrayListOf<NormalLine>()

            val curLine = split.lines[0]
            var curStartX: Double = curLine.minX // direction is lost at this point but this does not matter since split is complete
            var curStartY: Double = curLine.minY
            var curEndX: Double = curLine.maxX
            var curEndY: Double = curLine.maxY
            for (lineIndex in 1..(split.lines.size - 1)) {
                val line = split.lines[lineIndex]
                if (line.minX > curEndX || line.minY > curEndY) { // segment ended
                    result.add(NormalLine(curStartX, curStartY, curEndX, curEndY))
                    curStartX = line.minX
                    curStartY = line.minY
                }
                if (line.maxX > curEndX)
                    curEndX = line.maxX
                if (line.maxY > curEndY)
                    curEndY = line.maxY
            }
            result.add(NormalLine(curStartX, curStartY, curEndX, curEndY))

            split.lines.clear()
            split.lines.addAll(result)
        }

        fun isConvex(lines: List<NormalLine>): Boolean {
            if (lines.isEmpty())
                return false

            var angleSum = 0.0
            for (i in 1 until lines.size) {
                val normal1 = lines[i - 1].normal
                val normal2 = lines[i].normal

                val angle = normal1.signedAngleBetween(normal2)
                if (angle < 0)
                    return false

                //Log.d("TAGG", "Angle between ${lines[i-1]} and ${lines[i]} is ${getAngle(angle)}")

                angleSum += angle
                if (Math.abs(angleSum) > Math.PI * 2)
                    return false
            }
            return true
        }

        private fun getAngle(angle: Double): Double {
            return angle * 180.0 / Math.PI
        }

        fun getLeaf(node: TreeNode?, point: Vector): TreeNode? {
            if (node == null)
                return null

            if (node.convexLines.size > 0)
                return if (isInsideConvex(node.convexLines, point))
                    node
                else
                    null

            val side = node.lines[0].getSide(point.x, point.y)

            return when (side) {
                NormalLine.Side.COLLINEAR -> node
                NormalLine.Side.BACK -> getLeaf(node.back, point)
                NormalLine.Side.FRONT -> getLeaf(node.front, point)
            }
        }

        private fun isInsideConvex(convexLines: ArrayList<NormalLine>, point: Vector): Boolean {
            return convexLines.all {
                val side = it.getSide(point.x, point.y)
                side == NormalLine.Side.COLLINEAR || side == NormalLine.Side.FRONT
            }
        }
    }
}
