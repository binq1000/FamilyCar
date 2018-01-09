package me.tim.org.familycar_kotlin.data

import java.util.*

/**
 * Created by Nekkyou on 1-11-2017.
 */
class DataPoint(
        val time: Date,
        val latitude: Double?,
        val longitude: Double?,
        val obdData: ObdData
)