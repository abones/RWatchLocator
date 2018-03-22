package com.rubius.rwatchlocator

/**
 *
 */
class BspTree {
    companion object {
        private fun getSplittingLine(lines: List<NormalLine>): TreeNode? {
            var minSum = lines.size
            var bestSplit: TreeNode? = null
            for (line in lines) {
                val currentSplit = TreeNode()
                currentSplit.lines.add(line)

                for (secondLine in lines) {
                    if (secondLine === line)
                        continue

                    val sideStart = line.getSide(secondLine.startX, secondLine.startY)
                    val sideEnd = line.getSide(secondLine.endX, secondLine.endY)

                    if (sideStart == sideEnd)
                        placeLine(secondLine, currentSplit, sideStart)
                    else {
                        val point = line.getIntersection(secondLine, false)
                        if (point != null) {
                            if (secondLine.startX == point.x && point.y == secondLine.startY)
                                placeLine(secondLine, currentSplit, sideStart)
                            else {
                                val lineStart = NormalLine(secondLine.startX, secondLine.startY, point.x, point.y)
                                val lineEnd = NormalLine(point.x, point.y, secondLine.endX, secondLine.endY)
                                placeLine(lineStart, currentSplit, sideStart)
                                placeLine(lineEnd, currentSplit, sideEnd)
                            }
                        }
                    }
                }

                val sum = Math.abs(currentSplit.pendingFront.size - currentSplit.pendingBack.size)

                if (sum == 0)
                    return currentSplit // no point in looking further
                if (sum < minSum) {
                    minSum = sum
                    bestSplit = currentSplit
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

        private fun generateBspInternal(lines: List<NormalLine>): TreeNode? {
            if (lines.size > 2 && isConvex(lines)) {
                val result = TreeNode()
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
            /*if (split.lines.size <= 1)
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
                if (line.minX > curEndX && line.minY > curEndY) { // segment ended
                    result.add(NormalLine(curStartX, curStartY, curEndX, curEndY))
                    curStartX = line.minX
                    curStartY = line.minY
                }
                curEndX = line.maxX
                curEndY = line.maxY
            }
            result.add(NormalLine(curStartX, curStartY, curEndX, curEndY))

            split.lines.clear()
            split.lines.addAll(result)*/
        }

        private fun isConvex(lines: List<NormalLine>): Boolean {
            if (lines.isEmpty())
                return false
            var isPositive = false
            for (i in 1 until lines.size) {
                val crossSin = lines[i - 1].normal.crossSin(lines[i].normal)
                if (i == 1)
                    isPositive = crossSin >= 0
                else
                    if (crossSin >= 0 != isPositive)
                        return false
            }
            return true
        }

        fun generateBsp(lines: List<NormalLine>): TreeNode? {
            val newLines = getOptimizedLines(lines)
            return generateBspInternal(newLines)
        }

        private fun getOptimizedLines(lines: List<NormalLine>): List<NormalLine> {
            val sameVerticalLines = mapOf<Double, NormalLine>()
            val sameLines = mapOf<Pair<Double, Double>, NormalLine>()
            val result = arrayListOf<NormalLine>()
            return lines
        }
    }
}
