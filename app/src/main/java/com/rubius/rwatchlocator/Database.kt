package com.rubius.rwatchlocator

import android.graphics.PointF

/**
 *
 */
class Database {
    fun getRooms(): List<Room> {
        return listOf(
            Room(
                "301",
                listOf(
                    PointF(0.0f, 7.0f),
                    PointF(4.0f, 7.0f),
                    PointF(4.0f, 14.0f),
                    PointF(0.0f, 14.0f)
                )
            ),
            Room(
                "302",
                listOf(
                    PointF(0.0f, 0.0f),
                    PointF(8.0f, 0.0f),
                    PointF(8.0f, 7.0f),
                    PointF(0.0f, 7.0f)
                )
            ),
            Room(
                "302a",
                listOf(
                    PointF(8.0f, 0.0f),
                    PointF(10.0f, 0.0f),
                    PointF(10.0f, 5.0f),
                    PointF(8.0f, 5.0f)
                )
            ),
            Room(
                "302b",
                listOf(
                    PointF(8.0f, 5.0f),
                    PointF(10.0f, 5.0f),
                    PointF(10.0f, 7.0f),
                    PointF(8.0f, 7.0f)
                )
            )
        )
    }
}
