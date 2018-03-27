package com.rubius.rwatchlocator

/**
 *
 */
class TreeNode {
    var anchorPoints: ArrayList<AnchorPoint>? = null

    fun addAnchorPoint(anchorPoint: AnchorPoint) {
        if (anchorPoints == null)
            anchorPoints = arrayListOf(anchorPoint)
        else
            anchorPoints?.add(anchorPoint)
    }

    var front: TreeNode? = null
    var back: TreeNode? = null
    var room: Room? = null
    val lines = arrayListOf<NormalLine>()
    val convexLines = arrayListOf<NormalLine>()
    val pendingFront =  ArrayList<NormalLine>()
    val pendingBack = ArrayList<NormalLine>()
}
