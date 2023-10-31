package com.example.printermobile.ui.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.printermobile.R
import com.example.printermobile.core.document.documentType
import com.example.printermobile.core.print.test.PrintBluetoothTest
import com.example.printermobile.core.print.test.PrintUSBTest
import com.example.printermobile.core.print.test.PrintWifiTest
import com.example.printermobile.core.print.utils.printer1.Discrimination
import com.example.printermobile.core.printType.PrinterType
import com.example.printermobile.databinding.ActivityMissingPrintersBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.ViewModels.AddPrinterViewModel
import com.example.printermobile.ui.Views.Printer.MissingPrinterAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MissingPrintersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMissingPrintersBinding
    private lateinit var printers: Printers
    private var documentType: List<String> = listOf()
    private var printerType: String = PrinterType.WIFI.type
    private var selectedDocument: List<String> = listOf()
    private val addPrinterViewModel: AddPrinterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMissingPrintersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initListeners()
        initUI()
    }

    private fun initUI() {
        binding.rvDocumentType.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDocumentType.adapter = MissingPrinterAdapter(documentType) {
            selectedDocument = if (selectedDocument.contains(it)) {
                selectedDocument.minus(it)
            } else {
                selectedDocument.plus(it)
            }
        }
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
            try {
                if (checkForm()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            for (documents in selectedDocument) {
                                printers = Printers(
                                    null,
                                    "B",
                                    binding.etName.text.toString(),
                                    documents,
                                    binding.etCopies.text.toString().toInt(),
                                    binding.etCharacters.text.toString().toInt(),
                                    printerType,
                                    binding.etIPAddress.text.toString().trim(),
                                    null
                                )
                                if (binding.etPort.text.isNotBlank()) {
                                    printers.port = binding.etPort.text.toString().toInt()
                                }
                                val printerToSave = printers.createPrinterEntityFromPrinterModel()
                                addPrinterViewModel.onAdd(printerToSave)
                            }
                            val possibleSet =
                                applicationContext.getSharedPreferences("asd", 0)
                                    .getStringSet("Commands", setOf())
                            val possibleCommands = possibleSet?.toTypedArray()
                            if (!possibleCommands.isNullOrEmpty()) {
                                val st = addPrinterViewModel.getSystemType()
                                var m_ambiente = "comercios.illarli.com";
                                if (st != null) {
                                    m_ambiente = st.getName()
                                }
                                Discrimination(
                                    addPrinterViewModel.getAll(),
                                    m_ambiente,
                                    applicationContext
                                )(possibleCommands)
                            }
                        } catch (e: Exception) {
                            println(e)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e)
                Toast.makeText(this, "exception", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            val intent = Intent(this, ListPrintersActivity::class.java)
            startActivity(intent)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
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
        if (selectedDocument.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar un docuemnto", Toast.LENGTH_SHORT).show()
            println(selectedDocument)
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

    private fun initData() {
        val possibleSet =
            getSharedPreferences("asd", 0).getStringSet("Commands", setOf())
        val possibleCommands = possibleSet?.toTypedArray()
        val documentEnum: documentType = documentType()
        if (possibleCommands!!.isNotEmpty()) {
            for (command in possibleCommands) {
                val parts = command.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val possibleDocumentName = documentEnum.findDocumentByKey(parts[0])
                if (possibleDocumentName != null) {
                    documentType = documentType.plus(possibleDocumentName)
                }
            }
        } else {
            documentType = documentEnum.getDocuments()
        }
    }
}