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

    var bspRoot: TreeNode? = null
        private set(value) {
            field = value
        }

    private val internalAnchorPoints = arrayListOf<AnchorPoint>()
    val anchorPoints: List<AnchorPoint> = internalAnchorPoints
    fun addAnchorPoint(anchorPoint: AnchorPoint) {
        internalAnchorPoints.add(anchorPoint)
    }
}
