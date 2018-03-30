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
                                    val lineStart = NormalLine(currentLine.startX, currentLine.startY, point.x, point.y, currentLine.room)
                                    val lineEnd = NormalLine(point.x, point.y, currentLine.endX, currentLine.endY, currentLine.room)
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
                result.roomFront = lines[0].room
                result.convexLines.addAll(lines)
                return result
            }

            val split = getSplittingLine(lines) ?: return null


            assignRooms(split)

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

        private fun assignRooms(split: TreeNode) {
            val lines = split.lines
            val frontVector = lines[0].normal

            var frontRoomLine: NormalLine? = null
            var hasDifferentFrontRooms = false
            var backRoomLine: NormalLine? = null
            var hasDifferentBackRooms = false
            for (line in lines) {
                if (frontVector.dot(line.normal) == 1.0) {
                    if (!hasDifferentFrontRooms) {
                        if (frontRoomLine == null)
                            frontRoomLine = line
                        else if (!(frontRoomLine.room === line.room)) {
                            hasDifferentFrontRooms = true
                            frontRoomLine = null
                        }
                    }
                } else if (!hasDifferentBackRooms) {
                    if (backRoomLine == null)
                        backRoomLine = line
                    else if (!(backRoomLine.room === line.room)) {
                        hasDifferentBackRooms = true
                        backRoomLine = null
                    }
                }

                if (hasDifferentFrontRooms && hasDifferentBackRooms)
                    break
            }

            split.roomFront = frontRoomLine?.room
            split.roomBack = backRoomLine?.room
        }

        private fun isPositive(start: Double, end: Double): Boolean {
            return end - start >= 0
        }

        private fun getFirstCoordinate(originalStart: Double, originalEnd: Double, isXReferencePositive: Boolean): Double {
            return if (isXReferencePositive == isPositive(originalStart, originalEnd)) originalStart else originalEnd
        }

        private fun getLastCoordinate(originalStart: Double, originalEnd: Double, isXReferencePositive: Boolean): Double {
            return if (isXReferencePositive == isPositive(originalStart, originalEnd)) originalEnd else originalStart
        }

        private fun isCoordGreater(x1: Double, x2: Double, isPositiveDirection: Boolean): Boolean {
            return if (isPositiveDirection) x1 > x2 else x1 < x2
        }


        private fun optimizeLines(split: TreeNode) {
            if (split.lines.size <= 1)
                return

            val startLine = split.lines[0]
            val isXPositive = isPositive(startLine.startX, startLine.endX)
            val isYPositive = isPositive(startLine.startY, startLine.endY)

            split.lines.sortWith(compareBy({ if (isXPositive) it.minX else -it.maxX }, { if (isYPositive) it.minY else -it.maxY }))

            val result = arrayListOf<NormalLine>()

            val firstLine = split.lines[0]
            var curStartX: Double = getFirstCoordinate(firstLine.startX, firstLine.endX, isXPositive) // direction for other lines will be lost but this does not matter since split is complete
            var curStartY: Double = getFirstCoordinate(firstLine.startY, firstLine.endY, isYPositive)
            var curEndX: Double = getLastCoordinate(firstLine.startX, firstLine.endX, isXPositive)
            var curEndY: Double = getLastCoordinate(firstLine.startY, firstLine.endY, isYPositive)
            for (lineIndex in 1..(split.lines.size - 1)) {
                val line = split.lines[lineIndex]
                val firstX = getFirstCoordinate(line.startX, line.endX, isXPositive)
                val firstY = getFirstCoordinate(line.startY, line.endY, isYPositive)
                val lastX = getLastCoordinate(line.startX, line.endX, isXPositive)
                val lastY = getLastCoordinate(line.startY, line.endY, isYPositive)

                if (isCoordGreater(firstX, curEndX, isXPositive) || isCoordGreater(firstY, curEndY, isYPositive)) { // segment ended
                    result.add(NormalLine(curStartX, curStartY, curEndX, curEndY))
                    curStartX = firstX
                    curStartY = firstY
                }
                if (isCoordGreater(lastX, curEndX, isXPositive))
                    curEndX = lastX
                if (isCoordGreater(lastY, curEndY, isYPositive))
                    curEndY = lastY
            }
            result.add(NormalLine(curStartX, curStartY, curEndX, curEndY))

            split.lines.clear()
            split.lines.addAll(result)
        }

        fun isConvex(lines: List<NormalLine>): Boolean {
            if (lines.isEmpty()) // for our purposes a single line is a convex polygon
                return false

            var prevLine = lines[0]
            var angleSum = 0.0
            var lineIndex = 1
            while (lineIndex < lines.size) {
                var curLine = lines[lineIndex]

                val shouldInsertLine = prevLine.endX != curLine.startX || prevLine.endY != curLine.startY
                if (shouldInsertLine)
                    curLine = NormalLine(prevLine.endX, prevLine.endY, curLine.startX, curLine.startY)

                val normal1 = prevLine.normal
                val normal2 = curLine.normal

                val angle = normal1.signedAngleBetween(normal2)
                if (angle < 0 || angle >= Math.PI)
                    return false

                angleSum += angle
                if (Math.abs(angleSum) > Math.PI * 2)
                    return false

                prevLine = curLine
                if (!shouldInsertLine)
                    ++lineIndex
            }
            return true
        }

        private fun getAngle(angle: Double): Double {
            return angle * 180.0 / Math.PI
        }
    }
}
