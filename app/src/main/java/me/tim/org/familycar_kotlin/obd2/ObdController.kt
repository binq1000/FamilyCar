package me.tim.org.familycar_kotlin.obd2

import android.content.Context
import android.util.Log
import com.github.pires.obd.commands.SpeedCommand
import com.github.pires.obd.commands.engine.RPMCommand
import com.github.pires.obd.commands.protocol.EchoOffCommand
import com.github.pires.obd.commands.protocol.LineFeedOffCommand
import com.github.pires.obd.commands.protocol.SelectProtocolCommand
import com.github.pires.obd.commands.protocol.TimeoutCommand
import com.github.pires.obd.enums.ObdProtocols
import me.tim.org.familycar_kotlin.bluetooth.BluetoothController
import java.io.IOException

/**
 * Created by Nekkyou on 27-10-2017.
 */
class ObdController(private val context: Context) {
    private val bluetoothController: BluetoothController = BluetoothController(context)

    init {
        bluetoothController.connect()

        val socket = bluetoothController.socket
        if (socket != null) {
            try {
                EchoOffCommand().run(socket!!.getInputStream(), socket!!.getOutputStream())
                LineFeedOffCommand().run(socket!!.getInputStream(), socket!!.getOutputStream())
                TimeoutCommand(100).run(socket!!.getInputStream(), socket!!.getOutputStream())
                SelectProtocolCommand(ObdProtocols.AUTO).run(socket!!.getInputStream(), socket!!.getOutputStream())
            } catch (e: Exception) {
                // handle errors
            }

        } else {
            println("Socket not connected.")
        }
    }


    fun requestRpm(): String {
        var formattedRpm = ""
        val socket = bluetoothController.socket

        val rpmCommand = RPMCommand()
        val speedCommand = SpeedCommand()
        while (!Thread.currentThread().isInterrupted) {
            try {
                rpmCommand.run(socket.getInputStream(), socket.getOutputStream())
                speedCommand.run(socket.getInputStream(), socket.getOutputStream())

                //Handle results
                formattedRpm = rpmCommand.formattedResult
                Log.d(this.javaClass.name, "RPM: " + formattedRpm)
                Log.d(this.javaClass.name, "Speed: " + speedCommand.formattedResult)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }

        return formattedRpm
    }

    fun requestSpeed(): String {
        var formatted = ""
        val socket = bluetoothController.socket

        val speedCommand = SpeedCommand()
        while (!Thread.currentThread().isInterrupted) {
            try {
                speedCommand.run(socket.getInputStream(), socket.getOutputStream())

                //Handle results
                formatted = speedCommand.formattedResult
                Log.d(this.javaClass.name, "Speed: $formatted")
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }

        return formatted
    }
}