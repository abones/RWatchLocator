package com.rubius.rwatchlocator

import com.rubius.rwatchlocator.BspTree.Companion.generateBsp

/**
 *
 */
class Database {
    var rooms = listOf<Room>()
        set(value) {
            field = value

            for (room in value)
                room.createPath()

            bspRoot = generateBsp(value.flatMap { it.lines })
        }

    val anchorPoints = ArrayList<AnchorPoint>()
    var bspRoot: TreeNode? = null
        private set(value) {
            field = value
        }
}
