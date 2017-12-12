package me.tim.org.familycar_kotlin.data

import com.google.gson.Gson

/**
 * Created by Nekkyou on 5-12-2017.
 */
class Car(
        vin: String,
        rides: List<Ride>
) {
    companion object : JsonDecrypter {
        val gson = Gson()
        override fun fromJSON(json: String): Car {
            return gson.fromJson(json, Car::class.java)
        }

        override fun listFromJson(json: String): List<Car> {
            return gson.fromJson(json, Array<Car>::class.java).toList()
        }
    }
}