package com.rubius.rwatchlocator

import com.rubius.rwatchlocator.BspTree.Companion.generateBsp
import com.snatik.polygon.Line

/**
 *
 */
class Database {
    var rooms = listOf<Room>()
        set(value) {
            field = value

            val lines = arrayListOf<Line>()
            for (room in value)
                lines.addAll(room.polygon.sides)
            bspRoot = generateBsp(lines.map { l -> NormalLine(l) })
        }
    val anchorPoints = ArrayList<AnchorPoint>()
    var bspRoot: TreeNode? = null
        private set(value) {
            field = value
        }
    /*
    )*/
}
