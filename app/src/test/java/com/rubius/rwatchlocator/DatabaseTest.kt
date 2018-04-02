package com.rubius.rwatchlocator

import junit.framework.Assert
import org.junit.Test

/**
 *
 */
class DatabaseTest {
    class BspTreeFake : IBspTree {
        override fun generateBsp(lines: List<NormalLine>): TreeNode? {
            return null
        }
    }

    private val bspTreeFake = BspTreeFake()

    private val database = Database(bspTreeFake)

    @Test
    fun startsWithoutRooms() {
        Assert.assertEquals(0, database.rooms.size)
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
    fun singleBadRoomGetsAllAttention() {
        /*database.rooms = listOf(
            Room(

            )
        )*/
    }
}
