package com.rubius.rwatchlocator

/**
 *
 */
class TreeNode {
    var front: TreeNode? = null
    var back: TreeNode? = null
    val lines = arrayListOf<NormalLine>()
    val convexLines = arrayListOf<NormalLine>()
    val pendingFront =  ArrayList<NormalLine>()
    val pendingBack = ArrayList<NormalLine>()
}
