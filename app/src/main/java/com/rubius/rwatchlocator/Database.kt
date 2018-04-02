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

    private fun getLikeness(sample: Map<String, Int>, target: Map<String, Int>): Int {
        var sum = 0
        for (measurement in sample.entries) {
            val address = measurement.key
            val existingRssi = target[address]
            if (existingRssi != null)
                sum += Math.abs(measurement.value - existingRssi)
        }

        return sum
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

    private fun getDelta(sample: Map<String, Int>, target: Map<String, Int>): Int {
        return getLikeness(sample, target) - getUnlikeness(sample, target)
    }

    private fun normalize(value: Int, minDelta: Int, maxDelta: Int): Float {
        return (value - minDelta).toFloat() / (maxDelta - minDelta)
    }

    fun getRoomProbabilities(measurement: RssiMeasurement): List<Pair<Room, Float>> {
        val roomDeltas = IdentityHashMap<Room, Int>()

        var minDelta = 0
        var maxDelta = 0
        for (anchorPoint in anchorPoints) {
            val delta = getDelta(measurement.devices, anchorPoint.rssi.devices)

            if (delta < minDelta)
                minDelta = delta
            if (delta > maxDelta)
                maxDelta = delta

            val roomDelta = (roomDeltas[anchorPoint.room] ?: 0) + delta
            roomDeltas[anchorPoint.room] = roomDelta
        }

        return roomDeltas.map { Pair(it.key, normalize(it.value, minDelta, maxDelta)) }
    }
}
