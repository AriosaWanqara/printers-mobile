package com.example.printermobile.ui.Views

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dantsu.escposprinter.connection.usb.UsbOutputStream
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections
import com.example.printermobile.R
import com.example.printermobile.core.print.utils.printer1.Discrimination
import com.example.printermobile.core.printType.PrinterType
import com.example.printermobile.databinding.ActivityBackgroundPrinterBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.ViewModels.BackgroundPrinterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess


@AndroidEntryPoint
class BackgroundPrinterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBackgroundPrinterBinding
    private val backgroundPrinterViewModel: BackgroundPrinterViewModel by viewModels()
    private var m_ambiente: String? = null
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED,
                            false
                        )
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            print()
                        }
                        return
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        CoroutineScope(Dispatchers.IO).launch {
            val printers = backgroundPrinterViewModel.getAll()
            if (printers.find { it.type == PrinterType.USB.type } != null) {
                if (!checkUsbPermission()) {
                    print()
                }
            } else {
                print()
            }
        }
    }

    private fun initListeners() {
        binding.btnSaveMissingPrinters.setOnClickListener {
            val addIntent = Intent(this, MissingPrintersActivity::class.java)
            startActivity(addIntent)
        }
        binding.btnCancelMissingPrinters.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cerrar proceso de impresión")
            builder.setMessage("¿Esta seguro que desea cancelar esta acción? ")
            builder.setPositiveButton(
                "Cerrar"
            ) { _, _ ->
                moveTaskToBack(true);
                exitProcess(-1)
            }
            builder.setNegativeButton("Volver", null)
            builder.show()
        }
    }

    private fun checkUsbPermission(): Boolean {
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        application.registerReceiver(usbReceiver, filter)
        val usbConnection = UsbPrintersConnections.selectFirstConnected(applicationContext)
        val usbManager =
            application.getSystemService(AppCompatActivity.USB_SERVICE) as UsbManager
        if (usbConnection != null && usbManager != null) {
            val permissionIntent: PendingIntent = PendingIntent.getBroadcast(
                applicationContext, 0, Intent(ACTION_USB_PERMISSION),
                PendingIntent.FLAG_MUTABLE
            )
            return if (!usbManager.hasPermission(usbConnection.device)) {
                usbManager.requestPermission(usbConnection.device, permissionIntent)
                true
            } else {
                false
            }
        }
        return false
    }

    private suspend fun print(): Boolean {
        var result = true;
        try {
            val intent = intent
            val uri = intent.data

            if (uri == null) {
                result = false
                return false
            }
            val data = uri.schemeSpecificPart
            val commands = data.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val printers = backgroundPrinterViewModel.getAll()
            val st = backgroundPrinterViewModel.getSystemType()
            m_ambiente = "comercios.illarli.com"
            if (st != null) {
                m_ambiente = st.getName()
            }
            if (!Discrimination(printers, m_ambiente!!, this)(commands)) {
                result = false
                withContext(Dispatchers.Main) {
                    val fadeIn = AnimationUtils.loadAnimation(applicationContext,R.anim.fade_in)
                    binding.llPrintGift.visibility = View.INVISIBLE
                    binding.mcMissingPrintersWarningContainer.apply {
                        animation = fadeIn
                        visibility = View.VISIBLE
                    }
                }

            }
        } catch (e: InterruptedException) {
            Toast.makeText(this, "Error al imprimir", Toast.LENGTH_SHORT).show()
            result = false
            return false
        } finally {
            val isUsbPermissionSending =
                application.getSharedPreferences("usb", 0).getBoolean("permissions", false)
            if (result && !isUsbPermissionSending) {
                moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        return true
    }
}