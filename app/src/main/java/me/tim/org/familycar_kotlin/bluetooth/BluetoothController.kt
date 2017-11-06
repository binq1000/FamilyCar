package me.tim.org.familycar_kotlin.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.ParcelUuid
import com.github.pires.obd.commands.protocol.EchoOffCommand
import com.github.pires.obd.commands.protocol.LineFeedOffCommand
import com.github.pires.obd.commands.protocol.SelectProtocolCommand
import com.github.pires.obd.commands.protocol.TimeoutCommand
import com.github.pires.obd.enums.ObdProtocols
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
        val device = getDevice(getDeviceAddress())

        val uuids = ArrayList<UUID>()
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        uuids.add(uuid)

        device.uuids.mapTo(uuids) { it.uuid }

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
        // TODO search in the file, or search for the available ones with a certain name.
        return "00:1D:A5:68:98:8B"
    }
}