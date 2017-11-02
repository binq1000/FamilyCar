package me.tim.org.familycar_kotlin.data

import android.content.Context
import android.location.Location
import me.tim.org.familycar_kotlin.location.LocationController
import me.tim.org.familycar_kotlin.obd2.ObdController
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer

/**
 * Created by Nekkyou on 2-11-2017.
 */
class RideController(val context: Context) {
    val locationController: LocationController = LocationController(context)
    val obdController: ObdController = ObdController(context)
    val data = ArrayList<DataPoint>()
    private lateinit var timer: Timer

    fun startRide() {
        timer = fixedRateTimer(name = "dataPuller", initialDelay = 1000, period = 1000) {
            requestData()
        }
    }

    fun finishRide() : Ride {
        timer?.cancel()
        return Ride(Driver("Drivername"), data)
    }

    private fun requestData() {
        val random: Random = Random()
        val obdData = ObdData(random.nextInt(150), rpm = random.nextInt(8000))
        //val obdData = obdController.requestData()

        val datapoint = DataPoint(Calendar.getInstance(), locationController.lastLocation, obdData)
        data.add(datapoint)
    }
}