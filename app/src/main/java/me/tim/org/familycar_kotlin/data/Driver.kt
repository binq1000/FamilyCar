package me.tim.org.familycar_kotlin.data

import com.google.gson.Gson

/**
 * Created by Nekkyou on 1-11-2017.
 */
class Driver(val name: String) {
        companion object : JsonDecrypter {
            val gson = Gson()

            override fun fromJSON(json: String): Driver {
                return gson.fromJson(json, Driver::class.java)
            }
        }

}