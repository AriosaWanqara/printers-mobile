package com.example.printermobile.ui.Views

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.printermobile.core.document.documentType
import com.example.printermobile.core.print.test.PrintWifiTest
import com.example.printermobile.core.print.utils.printer1.Discrimination
import com.example.printermobile.databinding.ActivityAddPrinterBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.ViewModels.AddPrinterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddPrinterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPrinterBinding
    private lateinit var printers: Printers
    private val addPrinterViewModel: AddPrinterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()
        initUI()
        initListeners()
    }

    private fun initUI() {
        val documentType: documentType = documentType()
        val documentTypeSpinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            documentType.getDocuments()
        )
//        val fontTypeSpinnerAdapter = ArrayAdapter<String>(
//            this,
//            android.R.layout.simple_spinner_dropdown_item,
//            listOf("B", "A")
//        )
        binding.spDocumentType.adapter = documentTypeSpinnerAdapter
//        binding.spFontType.adapter = fontTypeSpinnerAdapter
    }

    private fun initListeners() {
        binding.btnPrintTest.setOnClickListener {
            try {
                if (binding.tbPrinterType.isChecked) {
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
                } else {
                    Toast.makeText(this, "Bluetooth Test", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "exception", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btnSave.setOnClickListener {
            try {
                if (checkForm()) {
                    printers = Printers(
                        null,
                        binding.etName.text.toString(),
                        "B",
                        binding.spDocumentType.selectedItem.toString().trim(),
                        binding.etCopies.text.toString().toInt(),
                        binding.etCharacters.text.toString().toInt(),
                        binding.tbPrinterType.isChecked,
                        binding.etIPAddress.text.toString().trim(),
                        null
                    )
                    if (binding.etPort.text.isNotBlank()) {
                        printers.port = binding.etPort.text.toString().toInt()
                    }
                    val printerToSave = printers.createPrinterEntityFromPrinterModel()
                    val possibleSet =
                        getSharedPreferences("asd", 0).getStringSet("Commands", setOf())
                    val possibleCommands = possibleSet?.toTypedArray()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            addPrinterViewModel.onAdd(printerToSave)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    applicationContext,
                                    "Impresora guardada",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (!possibleCommands.isNullOrEmpty()) {
                                val m_ambiente = "comercios.illarli.com";
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
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, ListPrintersActivity::class.java)
            startActivity(intent)
        }
        binding.tbPrinterType.setOnClickListener {
            if (binding.tbPrinterType.isChecked) {
                binding.etIPAddress.visibility = View.VISIBLE
                binding.etPort.visibility = View.VISIBLE
            } else {
                binding.etPort.visibility = View.GONE
                binding.etIPAddress.visibility = View.GONE
            }


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
        if (binding.etPort.text.isBlank() && binding.tbPrinterType.isChecked) {
            binding.etPort.error = "El puerto es requerido"
            error = false
        }
        if (binding.etIPAddress.text.isBlank() && binding.tbPrinterType.isChecked) {
            binding.etIPAddress.error = "La dirección es requerida"
            error = false
        }
        return error
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}