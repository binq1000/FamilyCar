package me.tim.org.familycar_kotlin.data

import com.google.gson.Gson

/**
 * Created by Nekkyou on 1-11-2017.
 */
data class Ride(
        val driver: Driver,
        val dataPoints: List<DataPoint>
) {
    companion object : JsonDecrypter {
        val gson = Gson()
        override fun fromJSON(json: String): Ride {
            return gson.fromJson(json, Ride::class.java)
        }
    }

}