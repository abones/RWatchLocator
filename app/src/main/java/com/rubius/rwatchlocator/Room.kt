package com.rubius.rwatchlocator

import android.graphics.Path
import com.snatik.polygon.Point
import com.snatik.polygon.Polygon

/**
 *
 */
class Room(val name: String, points: List<Point>) {
    val path = Path()
    val polygon: Polygon
    val anchorPoints = ArrayList<AnchorPoint>()

    init {
        val polygonBuilder = PolygonBuilderProxy.getPolygonBuilder()

        for ((i, point) in points.withIndex()) {
            if (i == 0)
                path.moveTo(point.x.toFloat(), point.y.toFloat())
            else
                path.lineTo(point.x.toFloat(), point.y.toFloat())

            polygonBuilder.addVertex(point)
        }
        path.close()
        polygon = polygonBuilder.build()
    }

    override fun toString(): String {
        return "Room(name='$name', path=${polygon.sides})"
    }

}
