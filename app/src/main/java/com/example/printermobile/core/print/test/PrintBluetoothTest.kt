package com.example.printermobile.core.print.test

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.widget.Toast
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.example.printermobile.core.print.EscposCoffee
import com.github.anastaciocintra.escpos.Style
import java.io.OutputStream
import java.util.UUID

class PrintBluetoothTest(private val context: Context) {

    private var streamBluetooth: OutputStream? = null
    operator fun invoke():Boolean {
        getOutputStream()
        if (streamBluetooth == null) {
            return false
        }
        try {
            val style = Style()
            val escposCoffee = EscposCoffee(style, this.streamBluetooth!!)
            escposCoffee.printBluetoothTest("B")
        }catch (e:Exception){
            Toast.makeText(context,"Error con la impresora",Toast.LENGTH_SHORT).show()
            return true
        }
        return true
    }
    @SuppressLint("MissingPermission")
    private fun getOutputStream() {
        val printers = BluetoothPrintersConnections()
        val bluetoothPrinters = printers.list
        if (!bluetoothPrinters.isNullOrEmpty()) {
            for (printer in bluetoothPrinters) {
                try {
                    printer.connect()
                    val btDevice: BluetoothDevice = printer.device
                    val bt =
                        btDevice.createRfcommSocketToServiceRecord(UUID.fromString(btDevice.uuids[0].toString()))
                    printer.disconnect()
                    bt.connect()
                    this.streamBluetooth = bt.outputStream
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}