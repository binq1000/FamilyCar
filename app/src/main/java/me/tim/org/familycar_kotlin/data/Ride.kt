package me.tim.org.familycar_kotlin.data

import java.util.*

/**
 * Created by Nekkyou on 1-11-2017.
 */
data class Ride(
        val driver: Driver,
        val dataPoints: List<DataPoint>) {
}