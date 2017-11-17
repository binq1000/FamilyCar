package me.tim.org.familycar_kotlin

import com.google.gson.Gson
import java.util.*

/**
 * Created by Nekkyou on 1-11-2017.
 */

fun <T> T?.nullCheck(block: (T) -> Unit) { if (this != null) block(this) }

fun <T> List<T>?.latest() : T? {
    if (this == null || this.isEmpty()) {
        return null
    }

    return this[this.lastIndex]
}

/**
 * Compare the dates of 2 Calendars, not including time.
 *
 * @return -1 when this is before, 0 when equal and 1 when after.
 */
fun Calendar.compare(other: Calendar) : Int {
    val c1Year = this.get(Calendar.YEAR)
    val c1Month = this.get(Calendar.MONTH)
    val c1Day = this.get(Calendar.DAY_OF_MONTH)

    val c2Year = other.get(Calendar.YEAR)
    val c2Month = other.get(Calendar.MONTH)
    val c2Day = other.get(Calendar.DAY_OF_MONTH)

    var returnValue = 0

    //Check year
    if (c1Year < c2Year) {
        returnValue = -1
    } else if (c1Year > c2Year) {
        returnValue = 1
    } else {
        //Years are equal
        //Check month
        if (c1Month < c2Month) {
            returnValue = -1
        } else if (c1Month > c2Month) {
            returnValue = 1
        } else {
            //Months are equal
            //Check day
            if (c1Day < c2Day) {
                returnValue = -1
            } else if (c1Day > c2Day) {
                returnValue = 1
            }
        }
    }

    //All are equal.
    return returnValue
}

fun Calendar.format() : String {
    val day = this.get(Calendar.DAY_OF_MONTH)
    val month = this.get(Calendar.MONTH)
    val year = this.get(Calendar.YEAR)

    return "$day/$month/$year"
}

fun Calendar.formatComplete() : String {
    val day = this.get(Calendar.DAY_OF_MONTH)
    val month = this.get(Calendar.MONTH)
    val year = this.get(Calendar.YEAR)
    val hour = this.get(Calendar.HOUR)
    val minute = this.get(Calendar.MINUTE)

    return "$day/$month/$year $hour:$minute"
}

fun <T> T.toJson() : String {
    val gson = Gson()
    return gson.toJson(this)
}