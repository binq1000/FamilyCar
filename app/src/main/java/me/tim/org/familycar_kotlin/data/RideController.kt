package me.tim.org.familycar_kotlin.data

import android.content.Context
import android.util.Log
import me.tim.org.familycar_kotlin.location.LocationController
import me.tim.org.familycar_kotlin.obd2.ObdController
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer

/**
 * Created by Nekkyou on 2-11-2017.
 *
 * Controlls the rides.
 */
class RideController(val context: Context) {
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

    fun finishRide() : Ride {
        timer.cancel()
        return Ride(Driver("Drivername"), data)
    }

    private fun requestData() {
        Log.d(this.javaClass.name, "Requesting data.")
        val obdData = obdController.requestData()

        val dataPoint = DataPoint(Calendar.getInstance(), locationController.lastLocation, obdData)
        data.add(dataPoint)
    }

    fun requestSpeed() : Int? {
        return obdController.requestSpeed()
    }
}