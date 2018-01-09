package me.tim.org.familycar_kotlin.uiClasses

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import me.tim.org.familycar_kotlin.R
import java.util.*

/**
 * Created by Nekkyou on 8-11-2017.
 */
class SelectDateFragment :
        DialogFragment(),
        DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val yy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)

        val datepicker = DatePickerDialog(activity, this, yy, mm, dd)
        datepicker.datePicker.maxDate = System.currentTimeMillis()

        return datepicker
    }

    override fun onDateSet(view: DatePicker?, yy: Int, mm: Int, dd: Int) {
        populateSetDate(yy, mm, dd)
    }

    fun populateSetDate(year: Int, month: Int, day: Int) {
        val formatted = "$day/$month/$year"

        //Set text to the formatted.
        val tvDate = activity.findViewById(R.id.tv_date) as TextView
        tvDate.text = formatted

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val historyActivity = activity as HistoryActivity
        historyActivity.filter(calendar)
    }

}