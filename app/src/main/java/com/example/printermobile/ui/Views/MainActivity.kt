package com.example.printermobile.ui.Views

import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.R
import androidx.lifecycle.whenStarted
import androidx.lifecycle.withStarted
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.printermobile.core.document.documentType
import com.example.printermobile.core.print.EscposCoffee
import com.example.printermobile.core.print.messageBuilder.BodyBuilder
import com.example.printermobile.core.print.messageBuilder.MediaBuilder
import com.example.printermobile.core.print.test.PrintWifiTest
import com.example.printermobile.databinding.ActivityMainBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.domain.services.GetAllPrinters
import com.example.printermobile.ui.Views.Printer.ListPrinterAdapter
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.output.TcpIpOutputStream
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        screenSplash.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView.view,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.view.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 200L
            slideUp.doOnEnd { splashScreenView.remove() }
            slideUp.start()
        }

        screenSplash.setKeepOnScreenCondition { false }
        initUI()
    }

    private fun initUI(){
        var printers:MutableList<Printers> = GetAllPrinters().getAll()
        binding.rvPrinterList.layoutManager = LinearLayoutManager(this)
        binding.rvPrinterList.adapter = ListPrinterAdapter(printers)
    }

//    private fun printWifi(host: String, port: Int) {
//        val printWifiTest = PrintWifiTest(host, port, "B")
//        printWifiTest.invoke()
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
//        return (packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH) || packageManager.hasSystemFeature(
//            PackageManager.FEATURE_BLUETOOTH_LE
//        ))
//    }
}