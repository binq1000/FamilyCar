package me.tim.org.familycar_kotlin.bluetooth

import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


/**
 * Created by Nekkyou on 27-10-2017.
 */
interface BluetoothSocketWrapper {
    @Throws(IOException::class)
    fun getInputStream(): InputStream

    @Throws(IOException::class)
    fun getOutputStream(): OutputStream

    fun getRemoteDeviceName(): String

    @Throws(IOException::class)
    fun connect()

    fun getRemoteDeviceAddress(): String

    @Throws(IOException::class)
    fun close()

    fun getUnderlyingSocket(): BluetoothSocket

}