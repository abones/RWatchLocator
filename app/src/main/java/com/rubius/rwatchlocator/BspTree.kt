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
            // TODO: exit condition

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
    }
}
