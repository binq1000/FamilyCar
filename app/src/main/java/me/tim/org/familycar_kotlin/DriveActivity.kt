package me.tim.org.familycar_kotlin

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_drive.*
import kotlinx.android.synthetic.main.content_drive.*
import me.tim.org.familycar_kotlin.obd2.ObdController
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class DriveActivity : AppCompatActivity() {

    //val controller: ObdController = ObdController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        val timer = fixedRateTimer(name = "updater", initialDelay = 1000, period = 1000) {
            updateData()
        }

    }

    fun updateData() {
        runOnUiThread {
            val random = Random()
            tvRpm.text = "Touren: ${random.nextInt(5000)}"
        }
    }


}
