package me.tim.org.familycar_kotlin.uiClasses

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.WeekViewEvent
import me.tim.org.familycar_kotlin.R

import kotlinx.android.synthetic.main.activity_planning.*
import kotlinx.android.synthetic.main.content_planning.*
import java.util.*

class PlanningActivity : AppCompatActivity(), MonthLoader.MonthChangeListener, WeekView.EmptyViewClickListener {
    val events = ArrayList<WeekViewEvent>()

    /**
     * Triggered when the users clicks on a empty space of the calendar.
     * @param time: [Calendar] object set with the date and time of the clicked position on the view.
     */
    override fun onEmptyViewClicked(time: Calendar?) {
        val newtime = time
        newtime?.add(Calendar.HOUR, 1)
        val event = WeekViewEvent(1, "Tim", time, newtime)
        events.add(event)
    }

    /**
     * Very important interface, it's the base to load events in the calendar.
     * This method is called three times: once to load the previous month, once to load the next month and once to load the current month.<br></br>
     * **That's why you can have three times the same event at the same place if you mess up with the configuration**
     * @param newYear : year of the events required by the view.
     * @param newMonth : month of the events required by the view <br></br>**1 based (not like JAVA API) --> January = 1 and December = 12**.
     * @return a list of the events happening **during the specified month**.
     */
    override fun onMonthChange(newYear: Int, newMonth: Int): MutableList<out WeekViewEvent> {
        events.add(WeekViewEvent(1, "Tim", 2017, 12, 19, 12, 2, 2017, 12, 19, 13, 2))
        return events
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planning)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        weekView.monthChangeListener = this
        weekView.emptyViewClickListener = this
    }

}
