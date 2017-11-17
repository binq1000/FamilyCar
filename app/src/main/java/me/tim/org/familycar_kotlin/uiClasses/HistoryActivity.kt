package me.tim.org.familycar_kotlin.uiClasses

import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics

import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*
import me.tim.org.familycar_kotlin.R
import me.tim.org.familycar_kotlin.compare
import me.tim.org.familycar_kotlin.data.DataPoint
import me.tim.org.familycar_kotlin.data.Driver
import me.tim.org.familycar_kotlin.data.ObdData
import me.tim.org.familycar_kotlin.data.Ride
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

        (1..50).mapTo(allItems) { generateItem(it) }
        items.addAll(allItems)

        adapter.notifyDataSetChanged()
    }

    private fun generateItem(i: Int): Ride {
        val drivers = arrayOf(
                "Tim Daniëls",
                "Daan Daniëls",
                "Jan Daniëls"
        )
        val driver = Driver(drivers[(Math.random() * drivers.size).toInt()])
        val data = ArrayList<DataPoint>()

        val startLocation = Location("Casteren")
        val endLocation = Location("Eindhoven")
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        start.add(Calendar.HOUR, (-1 * i))
        end.add(Calendar.HOUR, (-1 * i) + 1)

        data.add(DataPoint(start, startLocation, ObdData(0, 0)))
        data.add(DataPoint(end, endLocation, ObdData(0,0)))

        return Ride(driver, data)
    }

    fun filter(calendar: Calendar) {
        println("Filter method called")
        val newItems = ArrayList<Ride>()

        for(item in allItems) {
            if (item.dataPoints.first().time.compare(calendar) == 0) {
                newItems.add(item)
            } else if (item.dataPoints.last().time.compare(calendar) == 0) {
                newItems.add(item)
            }
        }

        items.clear()
        items.addAll(newItems)
        adapter.notifyDataSetChanged()
    }

}
