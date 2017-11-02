package me.tim.org.familycar_kotlin.data

import android.location.Location
import java.util.*

/**
 * Created by Nekkyou on 1-11-2017.
 */
class DataPoint(val time: Calendar, val location: Location?, val obdData: ObdData) {
}