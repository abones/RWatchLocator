package com.rubius.rwatchlocator

import com.snatik.polygon.Point
import junit.framework.Assert
import org.junit.Test

/**
 *
 */
class DatabaseTest {
    private val database = Database()
    private val noRooms = listOf<Room>()
    private val triangle = listOf(
        Room(
            "",
            listOf(
                Point(0.0, 0.0),
                Point(0.0, 1.0),
                Point(1.0, 0.0)
            )
        )
    )

    @Test
    fun startsWithoutRooms() {
        Assert.assertEquals(0, database.rooms.size)
    }

    @Test
    fun startsWithoutAnchors() {
        Assert.assertEquals(0, database.anchorPoints.size)
    }

    @Test
    fun startsWithoutBsp() {
        Assert.assertNull(database.bspRoot)
    }

    @Test
    fun assigningEmptyListLeavesNullBsp() {
        database.rooms = listOf()

        Assert.assertNull(database.bspRoot)
    }

    @Test
    fun triangularRoomCreatesOneNode() {
        //database.rooms =
    }
}
