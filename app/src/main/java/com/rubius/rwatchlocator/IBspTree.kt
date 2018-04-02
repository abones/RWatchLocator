package com.rubius.rwatchlocator

interface IBspTree {
    fun generateBsp(lines: List<NormalLine>): TreeNode?
}
