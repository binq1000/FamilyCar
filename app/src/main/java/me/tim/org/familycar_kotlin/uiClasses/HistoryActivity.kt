package me.tim.org.familycar_kotlin.uiClasses

import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics

import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*
import me.tim.org.familycar_kotlin.controller.HttpController
import me.tim.org.familycar_kotlin.R
import me.tim.org.familycar_kotlin.data.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {

    var allItems = ArrayList<Ride>()
    var items = ArrayList<Ride>()
    lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)

        setHandlers()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val dm = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(dm)
        val loc = IntArray(2)
        rv_rides.getLocationOnScreen(loc)
        val distance_to_bottom = dm.heightPixels - loc[1]

        val params = rv_rides.layoutParams
        params.height = distance_to_bottom
        rv_rides.requestLayout()
    }

    private fun setHandlers() {
        tv_date.setOnClickListener {
            val fragment = SelectDateFragment()
            fragment.show(fragmentManager, "Datepicker")

            println("Test line \n \n ")
        }

        val recyclerView = rv_rides
        adapter = CustomAdapter(this, items)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        //Get items for the list.
        getAllRides()
//        (1..50).mapTo(allItems) { generateItem(it) }
//        items.addAll(allItems)


    }

    private fun generateItem(i: Int): Ride {
        val drivers = arrayOf(
                "Tim Daniëls",
                "Daan Daniëls",
                "Jan Daniëls"
        )
        val driver = Driver(0, drivers[(Math.random() * drivers.size).toInt()])
        val data = ArrayList<DataPoint>()

        val startLocation = Location("Casteren")
        val endLocation = Location("Eindhoven")
        val start = DateTime.now()
        val end = DateTime.now()
        start.minusHours(-1 * i)
        end.minusHours((-1 * i) + 1)

        data.add(DataPoint(start.toDate(), startLocation.latitude, startLocation.longitude, ObdData(0, 0, 0f)))
        data.add(DataPoint(end.toDate(), endLocation.latitude, endLocation.longitude, ObdData(0,0, 0f)))

        return Ride(0, driver, data)
    }

    private fun getAllRides() {
        println("Getting all rides from NL01")
        doAsync {
            println("Starting Async")
            val json = HttpController.run("/car/NL01/rides")
            println(json)
            val rides = Ride.listFromJson(json)
            println("Found ${rides.size} Rides")
            allItems.addAll(rides)
            items.addAll(allItems)
            uiThread {
                println("Done with Async")
                adapter.notifyDataSetChanged()
            }

        }
    }

    fun filter(calendar: Calendar) {
        println("Filter method called")
        val date = calendar.time
        val newItems = ArrayList<Ride>()

        for(item in allItems) {
            if (item.dataPoints.first().time == date) {
                newItems.add(item)
            } else if (item.dataPoints.last().time == date) {
                newItems.add(item)
            }
        }

        items.clear()
        items.addAll(newItems)
        adapter.notifyDataSetChanged()
    }

}
