package me.tim.org.familycar_kotlin

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat

import kotlinx.android.synthetic.main.activity_drive.*
import kotlinx.android.synthetic.main.content_drive.*
import me.tim.org.familycar_kotlin.data.Driver
import me.tim.org.familycar_kotlin.data.DataPoint
import me.tim.org.familycar_kotlin.data.Ride
import me.tim.org.familycar_kotlin.data.RideController
import me.tim.org.familycar_kotlin.location.LocationController
import me.tim.org.familycar_kotlin.obd2.ObdController
import java.util.*
import kotlin.concurrent.fixedRateTimer

class DriveActivity : AppCompatActivity() {

    private var rideController: RideController? = null
    private var isDriving = false

    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        setHandlers()

        rideController = RideController(applicationContext)
    }


    private fun setHandlers() {
        btnStartDrive.setOnClickListener {
            isDriving = true
            rideController?.startRide()

            timer = fixedRateTimer(name = "uiUpdater", initialDelay = 1000, period = 1000) {
                updateData()
            }
        }
    }

    fun updateData() {
        runOnUiThread {
            val latestData = rideController?.data.latest()
            tvRpm.text = latestData?.obdData?.rpm.toString()

            tvLocation.text = "Location \n Lat: ${latestData?.location?.latitude} \n Long: ${latestData?.location?.longitude}"
        }
    }
}
