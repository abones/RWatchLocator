package com.rubius.rwatchlocator

import android.graphics.Path
import android.graphics.PointF

/**
 *
 */
class Room(val name: String, points: List<PointF>) {
    val path = Path()

    init {
        for ((i, point) in points.withIndex()) {
            if (i == 0)
                path.moveTo(point.x, point.y)
            else
                path.lineTo(point.x, point.y)
        }
    }

}
