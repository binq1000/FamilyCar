package me.tim.org.familycar_kotlin.data

import android.location.Location

/**
 * Created by Nekkyou on 1-11-2017.
 */
class RideAnalysis(val startLocation: Location, val endLocation: Location, val obdData: List<ObdData>) {
}