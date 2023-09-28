package com.example.printermobile.ui.Views

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.printermobile.core.Print.EscposCoffee
import com.example.printermobile.core.Print.MessageBuilder.BodyBuilder
import com.example.printermobile.core.Print.MessageBuilder.MediaBuilder
import com.example.printermobile.databinding.ActivityMainBinding
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.output.TcpIpOutputStream
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                printWifi("192.168.100.70", 9100)
            }
        }
    }

    private suspend fun printWifi(host: String, port: Int) {
        try {
            TcpIpOutputStream(host, port).use { outputStream ->
                var body: BodyBuilder = BodyBuilder(
                    listOf(
                        "buenas", "criaturitas del señor",
                        "buenas", "criaturitas del señor",
                        "buenas", "criaturitas del señor",
                        "buenas", "criaturitas del señor",
                        "buenas", "criaturitas del señor",
                        "buenas", "criaturitas del señor",
                        "buenas", "criaturitas del señor",
                        "buenas", "criaturitas del señor"
                    )
                )

                var media: MediaBuilder =
                    MediaBuilder(mapOf("imgU" to listOf("https://s-media-cache-ak0.pinimg.com/236x/ac/bb/d4/acbbd49b22b8c556979418f6618a35fd.jpg")))
                var style = Style()
                style.setFontName(Style.FontName.Font_B)
                var escposCoffee = EscposCoffee(style, outputStream)
//                escposCoffee.printBody(body)
                escposCoffee.printMedia(media)
//                escposCoffee.cut()
                escposCoffee.closeStream()
            }
        } catch (ex: IOException) {
            println("mal" + ex)
        }
    }


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