package me.tim.org.familycar_kotlin.uiClasses

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.abdallahalaraby.blink.Screenshot
import kotlinx.android.synthetic.main.activity_drive.*
import kotlinx.android.synthetic.main.content_drive.*
import me.tim.org.familycar_kotlin.R
import me.tim.org.familycar_kotlin.ScreenshotManager
import me.tim.org.familycar_kotlin.customExceptions.ObdConnectionFailedException
import me.tim.org.familycar_kotlin.data.DataWriter
import me.tim.org.familycar_kotlin.data.RideController
import me.tim.org.familycar_kotlin.latest
import java.util.*
import kotlin.concurrent.fixedRateTimer


class DriveActivity : AppCompatActivity() {

    private var rideController: RideController? = null
    private var isDriving = false

    private lateinit var timer: Timer
    private var counter = 0

    private val REQUEST_ENABLE_BT = 1035

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        setHandlers()
    }

    override fun onResume() {
        super.onResume()
        initController()
    }


    private fun initController() {
        Log.i(this.javaClass.name, "Initializing RideController \n ")
        if (rideController == null) {
            Thread(Runnable {
                try {
                    rideController = RideController(applicationContext)
                    runOnUiThread({ btnStartDrive.isEnabled = true})
                } catch (ofc: ObdConnectionFailedException) {
                    runOnUiThread({
                        tvRpm.text = "Trying to connect to Obd"
                        btnStartDrive.isEnabled = false
                        val handler = Handler()
                        handler.postDelayed({initController()}, 5000)
                    })
                }
            }).start()
        }
    }


    private fun setHandlers() {
        btnStartDrive.setOnClickListener {
            isDriving = true
            rideController?.startRide()

            timer = fixedRateTimer(name = "uiUpdater", initialDelay = 1000, period = 1000) {
                runOnUiThread { updateData() }
            }
        }


        btnStopDrive.setOnClickListener {
            if (isDriving) {
                isDriving = false
                val ride = rideController?.finishRide()
                //Save ride here.
                val writer = DataWriter(applicationContext)
                if (ride != null) {
                    writer.saveRide(ride)
                    Toast.makeText(applicationContext, "Ride saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateData() {
        runOnUiThread {
            val latestData = rideController?.data.latest()
            if (latestData != null) {
                tvRpm.text = "Touren: ${latestData.obdData.rpm.toString()}"
                tvSpeed.text = "Snelheid: ${latestData.obdData.speed.toString()}"
                tvLocation.text = "Location: \n Lat: ${latestData.location?.latitude} \n Long: ${latestData.location?.longitude}"
            }
            else {
                tvRpm.text = "LatestData is null"
                Log.i("DriveActvity", "Latest data is null")
            }

            if (counter < 10) {
                counter++
            }
            else {
                val manager: ScreenshotManager = ScreenshotManager(applicationContext, contentResolver)
                val bm = Screenshot.getInstance().takeScreenshotForScreen(this)
                manager.createScreenshot(bm)

                counter = 0
            }
        }
    }



}
