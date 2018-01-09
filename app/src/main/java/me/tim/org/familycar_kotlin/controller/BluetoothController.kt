package me.tim.org.familycar_kotlin.controller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import me.tim.org.familycar_kotlin.bluetooth.BluetoothConnector
import me.tim.org.familycar_kotlin.bluetooth.BluetoothSocketWrapper
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Nekkyou on 27-10-2017.
 */
class BluetoothController(private val contect: Context) {
    val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var connected: Boolean = false
    lateinit var socket: BluetoothSocketWrapper

    fun verifyBluetooth() : Boolean {
        if (adapter == null || !adapter.isEnabled) {
            return false
        }

        return true
    }

    fun getPairedDevices() : Set<BluetoothDevice> {
        return adapter.bondedDevices
    }

    fun getDevice(address: String) :BluetoothDevice {
        return adapter.getRemoteDevice(address)
    }

    fun connect() {
        //Make sure Bluetooth is enabled
        if (!verifyBluetooth()) adapter.enable()

        //Get the OBD2 device.
        val device = getDevice(getDeviceAddress())

        //Try connecting with possible uuids.
        val uuids = ArrayList<UUID>()
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        uuids.add(uuid)

        //device.uuids.mapTo(uuids) { it.uuid }

        //Try to connect to the device with the BluetoothConnector.
        val connector: BluetoothConnector = BluetoothConnector(device, false, adapter, uuids)
        try {
            socket = connector.connect()
            connected = true
        }
        catch (e: IOException) {
            println("Connection to Bluetooth failed")
            //e.printStackTrace()
        }
    }

    fun getDeviceAddress() : String {
        var address = ""
        val paired = adapter.bondedDevices
        paired
                .filter { it.name.startsWith("OBD") }
                .forEach { address = it.address }
        return address
    }
}