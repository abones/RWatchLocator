package com.rubius.rwatchlocator

import java.util.*

/**
 *
 */
class Database(private val bspTree: IBspTree) {
    var rooms = listOf<Room>()
        set(value) {
            field = value

            for (room in value)
                room.createPath()

            bspRoot = bspTree.generateBsp(value.flatMap { it.lines })
        }

    var bspRoot: TreeNode? = null
        private set(value) {
            field = value
        }

    val anchorPoints = arrayListOf<AnchorPoint>()

    companion object {
        const val DISTRIBUTION = 6.0
    }

    internal fun getLikeness(center: Double, value: Double): Double {
        return Math.exp(-Math.pow((value - center), 2.0) / (2.0 * DISTRIBUTION * DISTRIBUTION))
    }

    internal fun getLikeness(sample: Map<String, Int>, target: Map<String, Int>): Double? {
        if (target.isEmpty())
            return null

        var sum = 0.0
        for (measurement in sample.entries) {
            val address = measurement.key
            val existingRssi = target[address]
            if (existingRssi != null)
                sum += getLikeness(existingRssi.toDouble(), measurement.value.toDouble())
        }

        return sum / target.size
    }

    private fun getUnlikeness(sample: Map<String, Int>, target: Map<String, Int>): Int {
        var sum = 0
        for (targetItem in target.entries) {
            val address = targetItem.key
            val existingRssi = sample[address]
            if (existingRssi == null)
                sum += 100
        }

        return sum
    }

    private fun getDelta(sample: Map<String, Int>, target: Map<String, Int>): Double? {
        return getLikeness(sample, target)// - getUnlikeness(sample, target)
    }

    fun getRoomProbabilities(measurement: RssiMeasurement): Map<Room, Double> {
        val roomDeltas = IdentityHashMap<Room, Double>()

        for (anchorPoint in anchorPoints) {
            val delta = getDelta(measurement.devices, anchorPoint.rssi.devices) ?: continue

            val roomDelta = (roomDeltas[anchorPoint.room] ?: 0.0) + delta
            roomDeltas[anchorPoint.room] = roomDelta
        }

        var sum = 0.0
        val filteredRooms = roomDeltas.filter { it.value > 0.1 }
        for (room in filteredRooms)
            sum += room.value

        return filteredRooms.mapValues { it.value / sum }
    }
}
