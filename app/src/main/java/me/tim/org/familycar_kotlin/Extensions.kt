package me.tim.org.familycar_kotlin

/**
 * Created by Nekkyou on 1-11-2017.
 */

fun <T> T?.nullCheck(block: (T) -> Unit) { if (this != null) block(this) }