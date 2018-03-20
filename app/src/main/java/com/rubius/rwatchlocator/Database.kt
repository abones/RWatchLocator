package com.rubius.rwatchlocator

import com.snatik.polygon.Point

/**
 *
 */
class Database {
    fun getRooms(): List<Room> {
        return listOf(
            Room(
                "301",
                listOf(
                    Point(0.0, 7.0),
                    Point(4.0, 7.0),
                    Point(4.0, 14.0),
                    Point(0.0, 14.0)
                )
            ),
            Room(
                "302",
                listOf(
                    Point(0.0, 0.0),
                    Point(8.0, 0.0),
                    Point(8.0, 7.0),
                    Point(0.0, 7.0)
                )
            ),
            Room(
                "302a",
                listOf(
                    Point(8.0, 0.0),
                    Point(10.0, 0.0),
                    Point(10.0, 5.0),
                    Point(8.0, 5.0)
                )
            ),
            Room(
                "302b",
                listOf(
                    Point(8.0, 5.0),
                    Point(10.0, 5.0),
                    Point(10.0, 7.0),
                    Point(8.0, 7.0)
                )
            ),
            Room(
                "303",
                listOf(
                    Point(10.0, 0.0),
                    Point(18.0, 0.0),
                    Point(18.0, 7.0),
                    Point(10.0, 7.0)
                )
            ),
            Room(
                "304a",
                listOf(
                    Point(18.0, 0.0),
                    Point(22.0, 0.0),
                    Point(22.0, 7.0),
                    Point(18.0, 7.0)
                )
            ),
            Room(
                "304",
                listOf(
                    Point(22.0, 0.0),
                    Point(26.0, 0.0),
                    Point(26.0, 7.0),
                    Point(22.0, 7.0)
                )
            ),
            Room(
                "305",
                listOf(
                    Point(26.0, 0.0),
                    Point(30.0, 0.0),
                    Point(30.0, 7.0),
                    Point(26.0, 7.0)
                )
            ),
            Room(
                "306",
                listOf(
                    Point(30.0, 0.0),
                    Point(45.0, 0.0),
                    Point(45.0, 7.0),
                    Point(30.0, 7.0)
                )
            ),
            Room(
                "H2",
                listOf(
                    Point(10.0, 7.0),
                    Point(45.0, 7.0),
                    Point(45.0, 11.0),
                    Point(10.0, 11.0)
                )
            ),
            Room(
                "H2",
                listOf(
                    Point(10.0, 7.0),
                    Point(45.0, 7.0),
                    Point(45.0, 11.0),
                    Point(10.0, 11.0)
                )
            ),
            Room(
                "H1",
                listOf(
                    Point(4.0, 7.0),
                    Point(10.0, 7.0),
                    Point(10.0, 11.0),
                    Point(8.0, 11.0),
                    Point(8.0, 14.0),
                    Point(4.0, 14.0)
                )
            ),
            Room(
                "WC",
                listOf(
                    Point(8.0, 11.0),
                    Point(10.0, 11.0),
                    Point(10.0, 14.0),
                    Point(8.0, 14.0)
                )
            ),
            Room(
                "H3",
                listOf(
                    Point(-3.0, 14.0),
                    Point(10.0, 14.0),
                    Point(10.0, 18.0),
                    Point(-3.0, 18.0)
                )
            ),
            Room(
                "311",
                listOf(
                    Point(10.0, 11.0),
                    Point(14.0, 11.0),
                    Point(14.0, 18.0),
                    Point(10.0, 18.0)
                )
            ),
            Room(
                "310",
                listOf(
                    Point(14.0, 11.0),
                    Point(22.0, 11.0),
                    Point(22.0, 14.0),
                    Point(18.0, 14.0),
                    Point(18.0, 18.0),
                    Point(14.0, 18.0)
                )
            ),
            Room(
                "310a",
                listOf(
                    Point(18.0, 14.0),
                    Point(22.0, 14.0),
                    Point(22.0, 18.0),
                    Point(18.0, 18.0)
                )
            ),
            Room(
                "309",
                listOf(
                    Point(22.0, 11.0),
                    Point(34.0, 11.0),
                    Point(34.0, 18.0),
                    Point(22.0, 18.0)
                )
            ),
            Room(
                "308",
                listOf(
                    Point(34.0, 11.0),
                    Point(38.0, 11.0),
                    Point(38.0, 18.0),
                    Point(34.0, 18.0)
                )
            ),
            Room(
                "H4",
                listOf(
                    Point(38.0, 11.0),
                    Point(42.0, 11.0),
                    Point(42.0, 18.0),
                    Point(38.0, 18.0)
                )
            ),
            Room(
                "WC",
                listOf(
                    Point(42.0, 11.0),
                    Point(45.0, 11.0),
                    Point(45.0, 18.0),
                    Point(42.0, 18.0)
                )
            )
        )
    }
}
