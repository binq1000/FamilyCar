package me.tim.org.familycar_kotlin.data

/**
 * Created by Nekkyou on 16-11-2017.
 */
interface JsonDecrypter {
    fun fromJSON(json: String) : Any
}