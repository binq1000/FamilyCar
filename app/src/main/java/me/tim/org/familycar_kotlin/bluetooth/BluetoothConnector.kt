package me.tim.org.familycar_kotlin.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Created by Nekkyou on 27-10-2017.
 */
class BluetoothConnector(
        private val device: BluetoothDevice,
        private val secure: Boolean,
        private val adapter: BluetoothAdapter,
        private val uuidCandidates: List<UUID>
) {
    private lateinit var bluetoothSocket: BluetoothSocketWrapper
    private var candidate: Int = 0


    fun connect(): BluetoothSocketWrapper {
        var success = false
        while(selectSocket()) {
            adapter.cancelDiscovery()

            try {
                bluetoothSocket.connect()
                success = true
                break
            } catch (e: IOException) {
                Log.d("Bluetooth", "\n Initial Connect failed \n Attempting Fallback")

                //Trying the fallback
                try {
                    bluetoothSocket = FallbackBluetoothSocket(bluetoothSocket.getUnderlyingSocket())
                    Thread.sleep(500)
                    bluetoothSocket.connect()
                    success = true
                    break
                } catch (e1: InterruptedException) {
                    Log.w("BT", e1.message, e1)
                } catch (e1: IOException) {
                    Log.w("BT", "Fallback failed. Cancelling.")
                }
            }
        }

        if (!success) {
            throw IOException("Could not connect to device: ${device.address}")
        }

        return bluetoothSocket
    }

    private fun selectSocket(): Boolean {
        if (candidate >= uuidCandidates.size) {
            return false
        }

        var tmp: BluetoothSocket? = null
        var uuid: UUID = uuidCandidates.get(candidate++)
        Log.i("Bluetooth", "Attempting to connect to Protocol: $uuid")
        if (secure) {
            try {
                val methods = device.javaClass.methods
                tmp = device.javaClass.getMethod("createRfcommSocket", *arrayOf<Class<Int>>(Int::class.java)).invoke(device, 1) as BluetoothSocket
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
        else {
            try {
                val methods = device.javaClass.methods
                tmp = device.javaClass.getMethod("createInsecureRfcommSocket", *arrayOf<Class<*>>(Int::class.java)).invoke(device, 1) as BluetoothSocket
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }

        bluetoothSocket = NativeBluetoothSocket(tmp!!)

        return true
    }

    open class NativeBluetoothSocket(private val socket: BluetoothSocket) : BluetoothSocketWrapper {

        @Throws(IOException::class)
        override fun getInputStream(): InputStream {
            return socket.inputStream
        }

        @Throws(IOException::class)
        override fun getOutputStream(): OutputStream {
            return socket.outputStream
        }

        override fun getRemoteDeviceName(): String {
            return socket.remoteDevice.name
        }

        @Throws(IOException::class)
        override fun connect() {
            socket.connect()
        }

        override fun getRemoteDeviceAddress(): String {
            return socket.remoteDevice.address
        }

        @Throws(IOException::class)
        override fun close() {
            socket.close()
        }

        override fun getUnderlyingSocket(): BluetoothSocket {
            return socket
        }
    }

    inner class FallbackBluetoothSocket(private val socket: BluetoothSocket) : NativeBluetoothSocket(socket) {
        lateinit var fallbackSocket: BluetoothSocket
        val methodName = if(secure) "createRfcommSocket" else "createInsecureRfcommSocket"

        init {
            try {
                val clazz = socket.remoteDevice.javaClass
                val paramTypes = arrayOf<Class<*>>(Integer.TYPE)
                val m = clazz.getMethod(methodName, *paramTypes)
                val params = arrayOf<Any>(Integer.valueOf(1))
                fallbackSocket = m.invoke(socket.remoteDevice, *params) as BluetoothSocket
            }
            catch (e: Exception) {
                Log.i("Bluetooth", "Failed fallback: $e")
            }
        }

        @Throws(IOException::class)
        override fun getInputStream(): InputStream {
            return fallbackSocket.inputStream
        }

        @Throws(IOException::class)
        override fun getOutputStream(): OutputStream {
            return fallbackSocket.outputStream
        }

        @Throws(IOException::class)
        override fun connect() {
            fallbackSocket.connect()
        }

        @Throws(IOException::class)
        override fun close() {
            fallbackSocket.close()
        }
    }
}