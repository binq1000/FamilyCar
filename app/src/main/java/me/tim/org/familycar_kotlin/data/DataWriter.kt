package me.tim.org.familycar_kotlin.data

import android.content.Context
import com.google.gson.Gson
import android.net.ConnectivityManager
import me.tim.org.familycar_kotlin.controller.HttpController
import me.tim.org.familycar_kotlin.toJson
import org.jetbrains.anko.toast


/**
 * Created by Nekkyou on 1-11-2017.
 */
class DataWriter(val context: Context) {
    fun saveRide(vin: String, ride: Ride) {
        val gson = Gson()

        //Check for internet connection
        if (isNetworkAvailable()) {
            context.toast("Saving online")
            HttpController.post("/car/$vin/ride", ride.toJson())
        } else {
            context.toast("Saving offline")
            val filename = "rideSaves"


            //Get all old ones
            val reader = DataReader(context)
            var rides = reader.readRides()

            //Check if there are already rides.
            if (rides == null) {
                rides = HashMap<String, ArrayList<Ride>>()
                val rideList = ArrayList<Ride>()
                rideList.add(ride)
                rides.put(vin, rideList)
            } else {
                //If there are rides, check if the vin is known.
                val carRides = rides[vin]
                if (carRides == null) {
                    //If vin is unknown add it with the ride
                    val rideList = ArrayList<Ride>()
                    rideList.add(ride)
                    rides.put(vin, rideList)
                } else {
                    //If vin is known, add the ride
                    carRides.add(ride)
                }
            }

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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}