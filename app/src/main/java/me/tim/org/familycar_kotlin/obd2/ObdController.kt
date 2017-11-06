package me.tim.org.familycar_kotlin.obd2

import android.content.Context
import android.util.Log
import com.github.pires.obd.commands.SpeedCommand
import com.github.pires.obd.commands.engine.RPMCommand
import com.github.pires.obd.commands.protocol.*
import com.github.pires.obd.enums.ObdProtocols
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
            initalCommands()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun initalCommands() {
        val socket = bluetoothController.socket

        EchoOffCommand().run(socket.getInputStream(), socket.getOutputStream())
        LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream())
        HeadersOffCommand().run(socket.getInputStream(), socket.getOutputStream())
        SpacesOffCommand().run(socket.getInputStream(), socket.getOutputStream())
        TimeoutCommand(100).run(socket.getInputStream(), socket.getOutputStream())
        SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream())
    }


    fun requestRpm(): Int {
        var result = 0
        val socket = bluetoothController.socket

        val rpmCommand = RPMCommand()
        try {
            rpmCommand.run(socket.getInputStream(), socket.getOutputStream())
            //Handle results
            result = rpmCommand.rpm
            Log.d(this.javaClass.name, "RPM: $result")

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun requestSpeed(): Int {
        var result = 0
        val socket = bluetoothController.socket

        val speedCommand = SpeedCommand()
        try {
            speedCommand.run(socket.getInputStream(), socket.getOutputStream())
            //Handle results
            result = speedCommand.metricSpeed
            Log.d(this.javaClass.name, "Speed: $result")

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: Exception) {
            //Testing to catch all exceptions
            e.printStackTrace()
        }

        return result
    }

    fun requestData(): ObdData {
        Log.d(this.javaClass.name, "Requesting data!")
        val rpm = requestRpm()
        val speed = requestSpeed()


        return ObdData(speed, rpm)
    }
}