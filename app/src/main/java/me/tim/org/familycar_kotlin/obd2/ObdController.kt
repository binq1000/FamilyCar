package me.tim.org.familycar_kotlin.obd2

import android.content.Context
import android.util.Log
import com.github.pires.obd.commands.SpeedCommand
import com.github.pires.obd.commands.engine.RPMCommand
import me.tim.org.familycar_kotlin.bluetooth.BluetoothController
import me.tim.org.familycar_kotlin.data.ObdData
import java.io.IOException

/**
 * Created by Nekkyou on 27-10-2017.
 */
class ObdController(private val context: Context) {
    private val bluetoothController: BluetoothController = BluetoothController(context)

    init {
        try {
            bluetoothController.connect()


        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun requestRpm(): Int {
        var result = 0
        val socket = bluetoothController.socket

        val rpmCommand = RPMCommand()
        while (!Thread.currentThread().isInterrupted) {
            try {
                rpmCommand.run(socket.getInputStream(), socket.getOutputStream())
                //Handle results
                result = rpmCommand.rpm
                Log.d(this.javaClass.name, "RPM: $result")
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun requestSpeed(): Int {
        var result = 0
        val socket = bluetoothController.socket

        val speedCommand = SpeedCommand()
        while (!Thread.currentThread().isInterrupted) {
            try {
                speedCommand.run(socket.getInputStream(), socket.getOutputStream())
                //Handle results
                result = speedCommand.metricSpeed
                Log.d(this.javaClass.name, "Speed: $result")
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        return result
    }

    fun requestData(): ObdData {
        return ObdData(requestSpeed(), requestRpm())
    }
}