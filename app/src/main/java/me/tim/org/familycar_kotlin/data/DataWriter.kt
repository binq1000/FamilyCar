package me.tim.org.familycar_kotlin.data

import android.content.Context
import com.google.gson.Gson

/**
 * Created by Nekkyou on 1-11-2017.
 */
class DataWriter(val context: Context) {

    fun saveRide(ride: Ride) {
        val filename = "rideSaves - ${System.currentTimeMillis()}"
        val gson = Gson()

        //Get all old ones
        val reader = DataReader(context)
        var rides = reader.readRides()

        if (rides == null) {
            rides = ArrayList<Ride>()
        }

        rides.add(ride)

        try {
            val output = context.openFileOutput(filename, Context.MODE_PRIVATE)
            val json = gson.toJson(rides)
            output.write(json.toByteArray())
            output.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}