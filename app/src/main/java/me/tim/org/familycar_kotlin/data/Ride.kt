package me.tim.org.familycar_kotlin.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by Nekkyou on 1-11-2017.
 */
data class Ride(
        val id: Long,
        val driver: Driver,
        val dataPoints: List<DataPoint>
) {
    companion object : JsonDecrypter {
        val gson = GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm").create()
        override fun fromJSON(json: String): Ride {
            return gson.fromJson(json, Ride::class.java)
        }
        override fun listFromJson(json: String): List<Ride> {
            println("Converting json to list of rides")
            val rides = ArrayList<Ride>()
            try {
                rides.addAll(gson.fromJson(json, Array<Ride>::class.java))
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            return rides
        }
    }
}