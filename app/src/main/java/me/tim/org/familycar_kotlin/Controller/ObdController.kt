package me.tim.org.familycar_kotlin.Controller

import android.content.Context
import android.util.Log
import com.github.pires.obd.commands.SpeedCommand
import com.github.pires.obd.commands.control.VinCommand
import com.github.pires.obd.commands.engine.RPMCommand
import com.github.pires.obd.commands.fuel.FuelLevelCommand
import com.github.pires.obd.commands.protocol.*
import com.github.pires.obd.enums.ObdProtocols
import me.tim.org.familycar_kotlin.customExceptions.BluetoothDisabledException
import me.tim.org.familycar_kotlin.customExceptions.ObdConnectionFailedException
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
        } catch (btw: BluetoothDisabledException) {
            throw btw
        } catch (ocf: ObdConnectionFailedException) {
            //ocf.printStackTrace()
            throw ocf
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     *
     */
    fun initalCommands() {
        checkAvailable()

        val socket = bluetoothController.socket

        EchoOffCommand().run(socket.getInputStream(), socket.getOutputStream())
        LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream())
        HeadersOffCommand().run(socket.getInputStream(), socket.getOutputStream())
        SpacesOffCommand().run(socket.getInputStream(), socket.getOutputStream())
        TimeoutCommand(100).run(socket.getInputStream(), socket.getOutputStream())
        SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream())

        //Request VIM
        val vin = requestVin()
        //Now you know what car you are getting data for.
    }

    fun requestVin() : String{
        checkAvailable()

        var result = ""
        val socket = bluetoothController.socket

        val vinCommand = VinCommand()
        try {
            vinCommand.run(socket.getInputStream(), socket.getOutputStream())
            result = vinCommand.formattedResult
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    fun requestRpm(): Int {
        checkAvailable()

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
        checkAvailable()

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

    fun requesFuel(): Float {
        checkAvailable()

        var result = 0f
        val socket = bluetoothController.socket

        val fuelLevelCommand = FuelLevelCommand()
        try {
            fuelLevelCommand.run(socket.getInputStream(), socket.getOutputStream())
            //Handle results
            result = fuelLevelCommand.fuelLevel
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
        val fuel = requesFuel()

        return ObdData(speed, rpm, fuel)
    }

    fun checkAvailable() {
        if (!bluetoothController.connected) {
            throw ObdConnectionFailedException("Failed to connect to the obd sensor")
        }
    }

    fun isConnected() : Boolean {
        return bluetoothController.connected
    }
}