package com.rubius.rwatchlocator

/**
 *
 */
class TreeNode {
    var anchorPoints: ArrayList<AnchorPoint>? = null

    var front: TreeNode? = null
    var back: TreeNode? = null

    var roomFront: Room? = null
    var roomBack: Room? = null

    val lines = arrayListOf<NormalLine>()
    val convexLines = arrayListOf<NormalLine>()
    val pendingFront = ArrayList<NormalLine>()
    val pendingBack = ArrayList<NormalLine>()

    fun addAnchorPoint(anchorPoint: AnchorPoint) {
        if (anchorPoints == null)
            anchorPoints = arrayListOf(anchorPoint)
        else
            anchorPoints?.add(anchorPoint)
    }

    private fun isInsideConvex(point: Vector): Boolean {
        return convexLines.all {
            val side = it.getSide(point.x, point.y)
            side == NormalLine.Side.COLLINEAR || side == NormalLine.Side.FRONT
        }
    }

    fun isConvex(): Boolean = convexLines.size > 0

    fun getSide(point: Vector): NormalLine.Side? {
        return if (isConvex())
            if (isInsideConvex(point)) NormalLine.Side.FRONT else null
        else
            lines[0].getSide(point.x, point.y)
    }

    fun getLeaf(point: Vector): TreeNode? {
        val side = getSide(point)
        return when (side) {
            null -> null // encountered convex node but didn't fit inside it
            NormalLine.Side.COLLINEAR -> this
            NormalLine.Side.BACK -> if (back == null) this else back!!.getLeaf(point)
            NormalLine.Side.FRONT -> if (front == null) this else front!!.getLeaf(point)
        }
    }
}
