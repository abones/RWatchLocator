package com.rubius.rwatchlocator

import java.util.*
import kotlin.collections.HashMap

class RssiMeasurement(val createdAt: Date, devices: Map<String, Int>) {
    val devices: Map<String, Int> = HashMap(devices)
}
