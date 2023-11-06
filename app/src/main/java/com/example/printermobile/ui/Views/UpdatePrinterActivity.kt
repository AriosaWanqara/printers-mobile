package com.example.printermobile.ui.Views

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.printermobile.R
import com.example.printermobile.core.document.documentType
import com.example.printermobile.core.print.test.PrintBluetoothTest
import com.example.printermobile.core.print.test.PrintUSBTest
import com.example.printermobile.core.print.test.PrintWifiTest
import com.example.printermobile.core.printType.PrinterType
import com.example.printermobile.databinding.ActivityUpdatePrinterBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.ViewModels.ListPrintersViewModel
import com.example.printermobile.ui.ViewModels.UpdatePrinterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class UpdatePrinterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePrinterBinding
    private val updatePrinterViewModel: UpdatePrinterViewModel by viewModels()
    private var printerType: String = PrinterType.WIFI.type
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("printer")?.let {
            updatePrinterViewModel.onCreate(it.toInt())
        }

        initUI("Recibos")
        initListeners()

        updatePrinterViewModel.printer.observe(this, Observer {
            initData(it)
        })
    }

    private fun initData(printer: Printers) {
        val documentType: documentType = documentType()
        binding.etName.setText(printer.name)
        binding.etCharacters.setText(printer.charactersNumber.toString())
        binding.etCopies.setText(printer.copyNumber.toString())
        printer.port?.let { binding.etPort.setText(it.toString()) }
        printer.address?.let { binding.etIPAddress.setText(it) }
        printerType = printer.type
        setCardsSelectedState(printer.type)
        initUI(documentType.findDocumentByKey(printer.documentType)!!)
    }

    private fun initUI(name: String) {
        binding.topAppBar.menu.removeItem(R.id.miTourPlay)
        val documentType: documentType = documentType()
        val documentTypeSpinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            documentType.getDocuments()
        )
        binding.acDocumentType.setText(name)
        binding.acDocumentType.setAdapter(documentTypeSpinnerAdapter)
    }

    private fun initListeners() {
        binding.btnPrintTest.setOnClickListener {
            try {
                if (printerType == PrinterType.WIFI.type) {
                    if (binding.etPort.text.isNotBlank() && binding.etIPAddress.text.isNotBlank()) {
                        PrintWifiTest(
                            binding.etIPAddress.text.toString().trim(),
                            binding.etPort.text.toString().toInt(),
                            "B"
                        )()
                    } else {
                        Toast.makeText(this, "Debe ingresar la IP y el Puerto", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else if (printerType == PrinterType.BLUETOOTH.type) {
                    if (!PrintBluetoothTest(this)()) {
                        Toast.makeText(this, "Impresora no vinculada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (!PrintUSBTest(this)()) {
                        Toast.makeText(this, "Impresora no conectada", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(this, "exception", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btnSave.setOnClickListener {
            if (checkForm()) {
                try {
                    val printers = Printers(
                        updatePrinterViewModel.printer.value?.id,
                        "B",
                        binding.etName.text.toString(),
                        binding.acDocumentType.text.toString().trim(),
                        binding.etCopies.text.toString().toInt(),
                        binding.etCharacters.text.toString().toInt(),
                        printerType,
                        binding.etIPAddress.text.toString().trim(),
                        null
                    )
                    if (binding.etPort.text.isNotBlank()) {
                        printers.port = binding.etPort.text.toString().toInt()
                    }

                    if (printerType == PrinterType.BLUETOOTH.type || printerType == PrinterType.USB.type) {
                        printers.port = null
                        printers.address = null
                    }
                    val printerToSave = printers.createPrinterEntityFromPrinterModel()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            updatePrinterViewModel.onAdd(printerToSave)
                            withContext(Dispatchers.Main) {
                                val snackbar = Snackbar.make(
                                    binding.btnSave,
                                    "Impresora actualizada",
                                    Snackbar.LENGTH_SHORT
                                )
                                snackbar.setAction("Cerrar") {
                                    snackbar.dismiss()
                                }
                                snackbar.setActionTextColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.primary
                                    )
                                )
                                snackbar.show()
                            }
                        } catch (e: Exception) {
                            println(e)
                        }
                    }
                } catch (e: Exception) {
                    println(e)
                    Toast.makeText(this, "exception", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            val intent = Intent(this, ListPrintersActivity::class.java)
            startActivity(intent)
        }
        binding.topAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.miHelp -> {
                    val helpIntent = Intent(this, HelpPrinterActivity::class.java)
                    startActivity(helpIntent)
                    true
                }

                else -> false
            }
        }
        binding.cvWifi.setOnClickListener {
            setCardsSelectedState(PrinterType.WIFI.type)
        }
        binding.cvBluetooth.setOnClickListener {
            setCardsSelectedState(PrinterType.BLUETOOTH.type)
        }
        binding.cvUSB.setOnClickListener {
            setCardsSelectedState(PrinterType.USB.type)
        }
    }

    private fun checkForm(): Boolean {
        var error = true
        if (binding.etName.text.isBlank()) {
            binding.etName.error = "El nombre es requerido"
            error = false
        }
        if (binding.etCopies.text.isBlank()) {
            binding.etCopies.error = "El número de copias es requerido"
            error = false
        }
        if (binding.etCharacters.text.isBlank()) {
            binding.etCharacters.error = "El número de caracteres es requerido"
            error = false
        }
        if (binding.etPort.text.isBlank() && printerType == PrinterType.WIFI.type) {
            binding.etPort.error = "El puerto es requerido"
            error = false
        }
        if (binding.etIPAddress.text.isBlank() && printerType == PrinterType.WIFI.type) {
            binding.etIPAddress.error = "La dirección es requerida"
            error = false
        }
        return error
    }


    private fun inputVisibilityChange(param: Boolean) {
        if (param) {
            val fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in)
            binding.etIPAddress.apply {
                animation = fadeIn
                visibility = View.VISIBLE
            }
            binding.etPort.apply {
                animation = fadeIn
                visibility = View.VISIBLE
            }
            binding.tilIpAddress.apply {
                animation = fadeIn
                visibility = View.VISIBLE
            }
            binding.tilPort.apply {
                animation = fadeIn
                visibility = View.VISIBLE
            }
        } else {
            val fadeOut = AnimationUtils.loadAnimation(this,R.anim.fade_out)
            binding.etPort.apply {
                animation = fadeOut
                visibility = View.INVISIBLE
            }
            binding.etIPAddress.apply {
                animation = fadeOut
                visibility = View.INVISIBLE
            }
            binding.tilIpAddress.apply {
                animation = fadeOut
                visibility = View.INVISIBLE
            }
            binding.tilPort.apply {
                animation = fadeOut
                visibility = View.INVISIBLE
            }
        }
    }

    private fun setCardsSelectedState(params: String) {
        when (params) {
            PrinterType.WIFI.type -> {
                printerType = PrinterType.WIFI.type
                binding.cvWifi.setCardBackgroundColor(resources.getColor(R.color.primary))
                binding.ivWifi.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                binding.tvWifi.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )

                binding.cvBluetooth.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivBluetooth.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                binding.tvBluetooth.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                binding.cvUSB.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivUSB.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                binding.tvUSB.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                inputVisibilityChange(true)
            }

            PrinterType.BLUETOOTH.type -> {
                printerType = PrinterType.BLUETOOTH.type
                binding.cvBluetooth.setCardBackgroundColor(resources.getColor(R.color.primary))
                binding.ivBluetooth.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                binding.tvBluetooth.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )


                binding.cvWifi.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivWifi.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                binding.tvWifi.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )

                binding.cvUSB.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivUSB.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                binding.tvUSB.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                inputVisibilityChange(false)
            }

            else -> {
                printerType = PrinterType.USB.type
                binding.cvUSB.setCardBackgroundColor(resources.getColor(R.color.primary))
                binding.ivUSB.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                binding.tvUSB.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )


                binding.cvBluetooth.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivBluetooth.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                binding.tvBluetooth.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                binding.cvWifi.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivWifi.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                binding.tvWifi.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.darker_gray
                    )
                )
                inputVisibilityChange(false)
            }
        }
    }
}