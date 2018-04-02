package com.rubius.rwatchlocator

import junit.framework.Assert
import org.junit.Test
import java.util.*
import kotlin.collections.HashMap

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
    private val room1 = Room("", listOf())
    private val room2 = Room("", listOf())
    private val room3 = Room("", listOf())

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

    private fun createPoint(room: Room, rssi1: Int? = null, rssi2: Int? = null, rssi3: Int? = null): AnchorPoint {
        val measurement = createMeasurement(rssi1, rssi2, rssi3)
        return AnchorPoint(0.0, 0.0, measurement, room)
    }

    private fun createMeasurement(rssi1: Int? = null, rssi2: Int? = null, rssi3: Int? = null): RssiMeasurement {
        val rssi = HashMap<String, Int>()
        if (rssi1 != null)
            rssi["1"] = rssi1
        if (rssi2 != null)
            rssi["2"] = rssi2
        if (rssi3 != null)
            rssi["3"] = rssi3
        return RssiMeasurement(Date(0), rssi)
    }

    @Test
    fun noMeasurementsMeansNoMatch() {
        database.anchorPoints.add(createPoint(room1))
        database.anchorPoints.add(createPoint(room2))
        database.anchorPoints.add(createPoint(room3))
        val measurement = createMeasurement(-50, -50, -50)

        val result = database.getRoomProbabilities(measurement)

        Assert.assertEquals(0, result.size)
    }

    @Test
    fun weakMeasurementStillMatches() {
        database.anchorPoints.add(createPoint(room1, 0))
        val measurement = createMeasurement(-100) // normally this won't be matched

        val result = database.getRoomProbabilities(measurement) // but since this is the only match we got

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(1.0, result[room1])
    }

    @Test
    fun weakRoomGetsGoodMeasurement() {
        database.anchorPoints.add(createPoint(room1, -100))
        val measurement = createMeasurement(0) // normally this won't be matched

        val result = database.getRoomProbabilities(measurement) // but since this is the only match we got

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(1.0, result[room1])
    }

    @Test
    fun weakMeasurementSplitBetweenTwoRooms() {
        database.anchorPoints.add(createPoint(room1, 0))
        database.anchorPoints.add(createPoint(room2, 0))
        val measurement = createMeasurement(-100)

        val result = database.getRoomProbabilities(measurement) // but since this is the only match we got

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(0.5, result[room1])
        Assert.assertEquals(0.5, result[room2])
    }

    @Test
    fun measurementSplitBetweenTwoEqualRooms() {
        database.anchorPoints.add(createPoint(room1, -50))
        database.anchorPoints.add(createPoint(room2, -50))
        val measurement = createMeasurement(-20)

        val result = database.getRoomProbabilities(measurement)

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(0.5, result[room1]!!)
        Assert.assertEquals(0.5, result[room2]!!)
    }

    @Test
    fun measurementFavorsCloserButNotStrongEnoughRoom() {
        database.anchorPoints.add(createPoint(room1, -50))
        database.anchorPoints.add(createPoint(room2, -40))
        val measurement = createMeasurement(-38)

        val result = database.getRoomProbabilities(measurement)

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(0.125, result[room1]!!, 0.001)
        Assert.assertEquals(0.875, result[room2]!!, 0.001)
    }

    @Test
    fun measurementFavorsVeryNotStrongEnoughRoom() {
        database.anchorPoints.add(createPoint(room1, -50))
        database.anchorPoints.add(createPoint(room2, -10))
        val measurement = createMeasurement(-12)

        val result = database.getRoomProbabilities(measurement)

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(0.0, result[room1]!!, 0.0001)
        Assert.assertEquals(1.0, result[room2]!!, 0.0001)
    }

    @Test
    fun correctMatchingLikeness() {
        val value = database.getLikeness(-50.0, -50.0)

        Assert.assertEquals(1.0, value)
    }

    @Test
    fun almostZeroAtLessThanDistribution() {
        val value = database.getLikeness(-50.0, -50.0 - 20.0)

        Assert.assertEquals(0.0, value, 0.01)
    }

    @Test
    fun almostZeroAtGreaterThanDistribution() {
        val value = database.getLikeness(-50.0, -50.0 + 20.0)

        Assert.assertEquals(0.0, value, 0.01)
    }

    @Test
    fun normalizedForTwoMatchingPoints() {
        val sample = mapOf(Pair("1", -50), Pair("2", -40))
        val target = mapOf(Pair("1", -50), Pair("2", -40))

        val value = database.getLikeness(sample, target)

        Assert.assertEquals(1.0, value)
    }

    @Test
    fun almostZeroForTwoNonMatchingPoints() {
        val sample = mapOf(Pair("1", -50 + 20), Pair("2", -40 - 20))
        val target = mapOf(Pair("1", -50), Pair("2", -40))

        val value = database.getLikeness(sample, target)!!

        Assert.assertEquals(0.0, value, 0.01)
    }

    @Test
    fun zeroForUnknownPoints() {
        val sample = mapOf(Pair("3", -50), Pair("4", -40))
        val target = mapOf(Pair("1", -50), Pair("2", -40))

        val value = database.getLikeness(sample, target)

        Assert.assertEquals(0.0, value)
    }

    @Test
    fun maxValueForMatchingRoomOnly() {
        database.anchorPoints.add(createPoint(room1, rssi1 = -50))
        database.anchorPoints.add(createPoint(room2, rssi2 = -50)) // this one doesn't match
        val measurement = createMeasurement(-50)

        val result = database.getRoomProbabilities(measurement)

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(1.0, result[room1])
        Assert.assertEquals(0.0, result[room2])
    }

    @Test
    fun valuesSpreadForEquallyLikelyRooms() {
        database.anchorPoints.add(createPoint(room1, rssi1 = -50))
        database.anchorPoints.add(createPoint(room2, rssi2 = -50)) // this one doesn't match
        val measurement = createMeasurement(-50, -50)

        val result = database.getRoomProbabilities(measurement)

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(0.5, result[room1])
        Assert.assertEquals(0.5, result[room2])
    }
}
