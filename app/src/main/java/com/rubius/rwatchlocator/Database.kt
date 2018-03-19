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
            ),
            Room(
                "303",
                listOf(
                    PointF(10.0f, 0.0f),
                    PointF(18.0f, 0.0f),
                    PointF(18.0f, 7.0f),
                    PointF(10.0f, 7.0f)
                )
            ),
            Room(
                "304a",
                listOf(
                    PointF(18.0f, 0.0f),
                    PointF(22.0f, 0.0f),
                    PointF(22.0f, 7.0f),
                    PointF(18.0f, 7.0f)
                )
            ),
            Room(
                "304",
                listOf(
                    PointF(22.0f, 0.0f),
                    PointF(26.0f, 0.0f),
                    PointF(26.0f, 7.0f),
                    PointF(22.0f, 7.0f)
                )
            ),
            Room(
                "305",
                listOf(
                    PointF(26.0f, 0.0f),
                    PointF(30.0f, 0.0f),
                    PointF(30.0f, 7.0f),
                    PointF(26.0f, 7.0f)
                )
            ),
            Room(
                "306",
                listOf(
                    PointF(30.0f, 0.0f),
                    PointF(45.0f, 0.0f),
                    PointF(45.0f, 7.0f),
                    PointF(30.0f, 7.0f)
                )
            ),
            Room(
                "H2",
                listOf(
                    PointF(10.0f, 7.0f),
                    PointF(45.0f, 7.0f),
                    PointF(45.0f, 11.0f),
                    PointF(10.0f, 11.0f)
                )
            ),
            Room(
                "H2",
                listOf(
                    PointF(10.0f, 7.0f),
                    PointF(45.0f, 7.0f),
                    PointF(45.0f, 11.0f),
                    PointF(10.0f, 11.0f)
                )
            ),
            Room(
                "H1",
                listOf(
                    PointF(4.0f, 7.0f),
                    PointF(10.0f, 7.0f),
                    PointF(10.0f, 11.0f),
                    PointF(8.0f, 11.0f),
                    PointF(8.0f, 14.0f),
                    PointF(4.0f, 14.0f)
                )
            ),
            Room(
                "WC",
                listOf(
                    PointF(8.0f, 11.0f),
                    PointF(10.0f, 11.0f),
                    PointF(10.0f, 14.0f),
                    PointF(8.0f, 14.0f)
                )
            ),
            Room(
                "H3",
                listOf(
                    PointF(-3.0f, 14.0f),
                    PointF(10.0f, 14.0f),
                    PointF(10.0f, 18.0f),
                    PointF(-3.0f, 18.0f)
                )
            ),
            Room(
                "311",
                listOf(
                    PointF(10.0f, 11.0f),
                    PointF(14.0f, 11.0f),
                    PointF(14.0f, 18.0f),
                    PointF(10.0f, 18.0f)
                )
            ),
            Room(
                "310",
                listOf(
                    PointF(14.0f, 11.0f),
                    PointF(22.0f, 11.0f),
                    PointF(22.0f, 14.0f),
                    PointF(18.0f, 14.0f),
                    PointF(18.0f, 18.0f),
                    PointF(14.0f, 18.0f)
                )
            ),
            Room(
                "310a",
                listOf(
                    PointF(18.0f, 14.0f),
                    PointF(22.0f, 14.0f),
                    PointF(22.0f, 18.0f),
                    PointF(18.0f, 18.0f)
                )
            ),
            Room(
                "309",
                listOf(
                    PointF(22.0f, 11.0f),
                    PointF(34.0f, 11.0f),
                    PointF(34.0f, 18.0f),
                    PointF(22.0f, 18.0f)
                )
            ),
            Room(
                "308",
                listOf(
                    PointF(34.0f, 11.0f),
                    PointF(38.0f, 11.0f),
                    PointF(38.0f, 18.0f),
                    PointF(34.0f, 18.0f)
                )
            ),
            Room(
                "H4",
                listOf(
                    PointF(38.0f, 11.0f),
                    PointF(42.0f, 11.0f),
                    PointF(42.0f, 18.0f),
                    PointF(38.0f, 18.0f)
                )
            ),
            Room(
                "WC",
                listOf(
                    PointF(42.0f, 11.0f),
                    PointF(45.0f, 11.0f),
                    PointF(45.0f, 18.0f),
                    PointF(42.0f, 18.0f)
                )
            )
        )
    }
}
