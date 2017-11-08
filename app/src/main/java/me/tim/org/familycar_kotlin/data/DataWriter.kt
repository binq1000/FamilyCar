package me.tim.org.familycar_kotlin.data

import android.content.Context
import com.google.gson.Gson

/**
 * Created by Nekkyou on 1-11-2017.
 */
class DataWriter(val context: Context) {

    fun saveRide(ride: Ride) {
        val filename = "rideSaves"
        val gson = Gson()

        try {
            val output = context.openFileOutput(filename, Context.MODE_PRIVATE)
            val json = gson.toJson(ride)
            output.write(json.toByteArray())
            output.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}