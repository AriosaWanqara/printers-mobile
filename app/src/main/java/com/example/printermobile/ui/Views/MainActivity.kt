package com.example.printermobile.ui.Views

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.printermobile.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            Toast.makeText(this,"test",Toast.LENGTH_SHORT).show()
        }
    }

//    private fun printWifi(host: String, port: Int) {
//        try {
//            TcpIpOutputStream(host, port).use { outputStream ->
//                val escpos = EscPos(outputStream)
//                escpos.info()
//            }
//        } catch (ex: IOException) {
//            println("mal" + ex)
//        }
//    }


//    @SuppressLint("MissingPermission")
//    private fun getOutputStreamFromBluetoothDevice(): OutputStream? {
//        val printers = BluetoothPrintersConnections()
//        val bluetoothPrinters = printers.list
//
//        if (!bluetoothPrinters.isNullOrEmpty()) {
//            for (printer in bluetoothPrinters) {
//                try {
//                    printer.connect()
//                    val btDevice: BluetoothDevice = printer.device
//                    val bt =
//                        btDevice.createRfcommSocketToServiceRecord(UUID.fromString(btDevice.uuids[0].toString()))
//                    printer.disconnect()
//                    bt.connect()
//                    return bt.outputStream
//                } catch (e: EscPosConnectionException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        return null
//    }

//    private fun printBTC() {
//        if (checkBTPermissions()) {
//            val outputStream = getOutputStreamFromBluetoothDevice()
//            if (outputStream != null) {
//                val escpos = EscPos(outputStream)
//                escpos.writeLF("Hello Wold")
//                escpos.feed(5)
//                escpos.cut(EscPos.CutMode.FULL)
//                escpos.close()
//            }
//        }
//    }
//
//    private fun checkBTPermissions(): Boolean {
//        return true;
//    }
}