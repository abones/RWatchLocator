package com.rubius.rwatchlocator

import com.snatik.polygon.Line
import com.snatik.polygon.Point

/**
 *
 */
class Database {
    val anchorPoints = arrayListOf(AnchorPoint(4.0, 4.0))
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
        ),
        Room(
            "306",
            listOf(
                Point(30.0, 0.0),
                Point(45.0, 0.0),
                Point(45.0, 7.0),
                Point(30.0, 7.0)
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
        ),
        Room(
            "H2",
            listOf(
                Point(10.0, 7.0),
                Point(45.0, 7.0),
                Point(45.0, 11.0),
                Point(10.0, 11.0)
            )
        ),
        Room(
            "H1",
            listOf(
                Point(4.0, 7.0),
                Point(10.0, 7.0),
                Point(10.0, 11.0),
                Point(8.0, 11.0),
                Point(8.0, 14.0),
                Point(4.0, 14.0)
            )
        ),
        Room(
            "WC",
            listOf(
                Point(8.0, 11.0),
                Point(10.0, 11.0),
                Point(10.0, 14.0),
                Point(8.0, 14.0)
            )
        ),
        Room(
            "H3",
            listOf(
                Point(-3.0, 14.0),
                Point(10.0, 14.0),
                Point(10.0, 18.0),
                Point(-3.0, 18.0)
            )
        ),
        Room(
            "311",
            listOf(
                Point(10.0, 11.0),
                Point(14.0, 11.0),
                Point(14.0, 18.0),
                Point(10.0, 18.0)
            )
        ),
        Room(
            "310",
            listOf(
                Point(14.0, 11.0),
                Point(22.0, 11.0),
                Point(22.0, 14.0),
                Point(18.0, 14.0),
                Point(18.0, 18.0),
                Point(14.0, 18.0)
            )
        ),
        Room(
            "310a",
            listOf(
                Point(18.0, 14.0),
                Point(22.0, 14.0),
                Point(22.0, 18.0),
                Point(18.0, 18.0)
            )
        ),
        Room(
            "309",
            listOf(
                Point(22.0, 11.0),
                Point(34.0, 11.0),
                Point(34.0, 18.0),
                Point(22.0, 18.0)
            )
        ),
        Room(
            "308",
            listOf(
                Point(34.0, 11.0),
                Point(38.0, 11.0),
                Point(38.0, 18.0),
                Point(34.0, 18.0)
            )
        ),
        Room(
            "H4",
            listOf(
                Point(38.0, 11.0),
                Point(42.0, 11.0),
                Point(42.0, 18.0),
                Point(38.0, 18.0)
            )
        ),
        Room(
            "WC",
            listOf(
                Point(42.0, 11.0),
                Point(45.0, 11.0),
                Point(45.0, 18.0),
                Point(42.0, 18.0)
            )
        )
    )

    private fun getNormalDot(line: NormalLine, secondLine: NormalLine): Double {
        return line.normal.x * secondLine.normal.x + line.normal.y + secondLine.normal.y
    }

    private fun getSplittingLine(lines: List<NormalLine>): TreeNode? {
        var minSum = lines.size
        var bestSplit: TreeNode? = null
        for (line in lines) {
            val currentSplit = TreeNode()
            currentSplit.lines.add(line)

            for (secondLine in lines) {
                if (secondLine === line)
                    continue

                val dot = getNormalDot(line, secondLine)
                when {
                    dot == 1.0 -> currentSplit.lines.add(secondLine)
                    dot < 0 -> currentSplit.pendingFront.add(secondLine)
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

    private fun generateBsp(lines: List<NormalLine>): TreeNode? {
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

    fun createBsp() {
        val lines = arrayListOf<Line>()
        for (room in rooms)
            lines.addAll(room.polygon.sides)
        generateBsp(lines.map { l -> NormalLine(l) })
    }
}
