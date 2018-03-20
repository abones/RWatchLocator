package com.rubius.rwatchlocator

import com.snatik.polygon.Line

/**
 *
 */
data class NormalLine(val line: Line) {
    val normal = Vector(-(line.end.y - line.start.y), line.end.x - line.start.x).normalize()

    override fun toString(): String {
        return "NormalLine(line=$line, normal=$normal)"
    }
}
