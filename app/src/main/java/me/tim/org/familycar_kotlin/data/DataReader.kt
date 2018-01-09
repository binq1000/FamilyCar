package me.tim.org.familycar_kotlin.data

import android.content.Context
import com.google.gson.Gson
import java.io.FileNotFoundException

/**
 * Created by Nekkyou on 1-11-2017.
 */
class DataReader(val context: Context) {
    fun readRides(): HashMap<String, ArrayList<Ride>> {
        var rides: HashMap<String, ArrayList<Ride>>? = HashMap<String, ArrayList<Ride>>()
        val filename = "rideSaves"

        try {
            val reader = context.openFileInput(filename)
            val gson = Gson()
            rides = gson.fromJson(reader.reader(), HashMap<String, ArrayList<Ride>>()::class.java)
        }
        catch (fnfe: FileNotFoundException) {
            //File is not found, rides will remain null.
            return HashMap<String, ArrayList<Ride>>()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return rides!!
    }
}