package me.tim.org.familycar_kotlin

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat

import kotlinx.android.synthetic.main.activity_drive.*
import kotlinx.android.synthetic.main.content_drive.*
import me.tim.org.familycar_kotlin.data.Driver
import me.tim.org.familycar_kotlin.data.ObdData
import me.tim.org.familycar_kotlin.data.Ride
import me.tim.org.familycar_kotlin.data.RideAnalysis
import me.tim.org.familycar_kotlin.location.LocationController
import me.tim.org.familycar_kotlin.obd2.ObdController
import java.util.*
import kotlin.concurrent.fixedRateTimer

class DriveActivity : AppCompatActivity() {

    var controller: ObdController? = null
    var locationController: LocationController? = null
    var isNotified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        //controller = try { ObdController(this) } catch (e: Exception) { null }
        locationController = LocationController(applicationContext)

        val timer = fixedRateTimer(name = "updater", initialDelay = 1000, period = 1000) {
            updateData()
        }

    }

    fun updateData() {
        runOnUiThread {

            if (controller == null && !isNotified) {
                val resultIntent = Intent(this, HomeActivity::class.java)
                val pi = PendingIntent.getActivity(this, 0, resultIntent, 0)
                val builder =
                        NotificationCompat.Builder(applicationContext)
                        .setContentIntent(pi)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setTicker("Testing")
                        .setAutoCancel(true)
                        .setContentTitle("Connect failed")
                        .setContentText("Failed to connect to the obd2 sensor")
                        .setSmallIcon(R.drawable.ic_directions_car_black_24dp)

                val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(0, builder.build())
                isNotified = true
            }

            val random = Random()
            tvRpm.text = "Touren: ${random.nextInt(5000)}"

            val locatie = locationController?.lastLocation
            locatie.nullCheck { tvLocation.text = "Locatie: \n Lat ${it.latitude} \n Long ${it.longitude}" }

        }
    }

    fun onRideFinished() {
        val driver = Driver("driverName")
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()

        val startLocation = locationController?.lastLocation
        val endLocation = locationController?.lastLocation
        val obdData = ArrayList<ObdData>()

        val analysis = RideAnalysis(startLocation!!, endLocation!!, obdData)

        val ride = Ride(driver, start, end, analysis)
    }
}
