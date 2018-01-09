package me.tim.org.familycar_kotlin.controller

import android.content.Context
import android.util.Log
import me.tim.org.familycar_kotlin.data.DataPoint
import me.tim.org.familycar_kotlin.data.Driver
import me.tim.org.familycar_kotlin.data.Ride
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer

/**
 * Created by Nekkyou on 2-11-2017.
 *
 * Controlls the rides.
 */
class RideController(val context: Context) {
    var vin: String = ""

    val locationController: LocationController = LocationController(context)
    val obdController: ObdController = ObdController(context)
    val data = ArrayList<DataPoint>()
    private lateinit var timer: Timer

    /**
     * Starting a new ride.
     */
    fun startRide() {
        if (obdController.isConnected()) {
            timer = fixedRateTimer(name = "dataPuller", initialDelay = 1000, period = 1000) {
                requestData()
            }
        }
    }

    fun finishRide() : Pair<String, Ride> {
        timer.cancel()
        return Pair<String, Ride>(vin, Ride(0, Driver(0, "Drivername"), data))
    }

    private fun requestData() {
        Log.d(this.javaClass.name, "Requesting data.")
        val obdData = obdController.requestData()
        if (vin == "") {
            vin = obdController.requestVin()
        }

        val dataPoint = DataPoint(DateTime.now().toDate(), locationController.lastLocation?.latitude, locationController.lastLocation?.longitude, obdData)
        data.add(dataPoint)
    }

    fun requestSpeed() : Int? {
        return obdController.requestSpeed()
    }
}