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

                    val side = line.getSide(secondLine.startX, secondLine.startY)
                    when (side) {
                        NormalLine.Side.FRONT -> currentSplit.pendingFront.add(secondLine)
                        NormalLine.Side.BACK -> currentSplit.pendingBack.add(secondLine)
                        NormalLine.Side.COLLINEAR -> currentSplit.lines.add(secondLine)
                    }
                }

                val sum = Math.abs(currentSplit.pendingFront.size - currentSplit.pendingBack.size)

                if (sum == 0)
                    return currentSplit // no point in looking further
                else if (sum < minSum) {
                    minSum = sum
                    bestSplit = currentSplit
                }
            }

            return bestSplit
        }

        fun generateBsp(lines: List<NormalLine>): TreeNode? {
            if (isConvex(lines)) {
                val result = TreeNode()
                result.lines.addAll(lines)
                return result
            }

            val split = getSplittingLine(lines) ?: return null

            // TODO: optimize split.lines
            // TODO: split anything intersected

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
    }
}
