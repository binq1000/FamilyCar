package me.tim.org.familycar_kotlin.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.ParcelUuid
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Nekkyou on 27-10-2017.
 */
class BluetoothController(private val contect: Context) {
    val adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
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

    fun connect(address: String) {
        val device = getDevice(address)

        val uuids = ArrayList<UUID>()
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        uuids.add(uuid)

        device.uuids.mapTo(uuids) { it.uuid }

        val connector: BluetoothConnector = BluetoothConnector(device, false, adapter, uuids)
        try {
            connector.connect()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }
}