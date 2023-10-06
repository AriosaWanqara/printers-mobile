package com.example.printermobile.ui.Views

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.printermobile.databinding.ActivityMainBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.ViewModels.ListPrintersViewModel
import com.example.printermobile.ui.Views.Printer.ListPrinterAdapter
import com.example.printermobile.ui.Views.Printer.SwipeHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: ListPrintersViewModel by viewModels()
    private var printers: List<Printers> = listOf()
    private val adapter = ListPrinterAdapter(printers) {it->
        val id = it.id
        redirect(id!!)
    }

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

        hideSystemUI()
        initUI()
        initListeners()
        mainViewModel.onCreate()
        mainViewModel.printers.observe(this, Observer {
            printers = listOf()
            it.map { printer ->
                printers = printers.plus(printer)
            }
                adapter.updateList(printers)
        })
        screenSplash.setKeepOnScreenCondition { false }
    }

    private fun initUI() {
        binding.rvPrinterList.layoutManager = LinearLayoutManager(this)
        binding.rvPrinterList.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.rvPrinterList) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                val deleteButton = deleteButton(position)
                buttons = listOf(deleteButton)
                return buttons
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvPrinterList)

    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    CoroutineScope(Dispatchers.IO).launch{
                        mainViewModel.onDeletePrinter(printers[position].id!!)
                    }

                }
            })
    }
    private fun initListeners() {
        binding.fabAddPrinter.setOnClickListener {
            val intent = Intent(this, AddPrinterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun redirect(id:Int){
        val intentN = Intent(this,UpdatePrinterActivity::class.java)
        intentN.putExtra("printer", id.toString())
        startActivity(intentN)
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
//        return (packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH) || packageManager.hasSystemFeature(
//            PackageManager.FEATURE_BLUETOOTH_LE
//        ))
//    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}