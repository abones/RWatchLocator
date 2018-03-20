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

                    val dot = line.normal.dot(secondLine.normal)
                    when {
                        dot - 1.0f < 0.0001 -> currentSplit.lines.add(secondLine)
                        dot < 0.0 -> currentSplit.pendingFront.add(secondLine)
                        else -> currentSplit.pendingBack.add(secondLine)
                    }
                }

                val sum = currentSplit.pendingFront.size - currentSplit.pendingBack.size

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

            split.front = generateBsp(split.pendingFront)
            split.pendingFront.clear()
            split.back = generateBsp(split.pendingBack)
            split.pendingBack.clear()

            return split
        }
    }
}
