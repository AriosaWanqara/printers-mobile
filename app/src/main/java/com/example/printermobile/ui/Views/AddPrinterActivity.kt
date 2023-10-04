package com.example.printermobile.ui.Views

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.printermobile.core.document.documentType
import com.example.printermobile.core.print.test.PrintWifiTest
import com.example.printermobile.data.database.dao.PrinterDAO
import com.example.printermobile.databinding.ActivityAddPrinterBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.domain.services.AddPrinters
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddPrinterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPrinterBinding
    private lateinit var printers: Printers

    //TODO: inject this dependency
    private lateinit var addPrinters:AddPrinters
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListeners()
    }

    private fun initUI() {
        val documentType: documentType = documentType()
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            documentType.getDocuments()
        )
        binding.spDocumentType.adapter = adapter
    }

    private fun initListeners() {
        binding.btnPrintTest.setOnClickListener {
            try {
                if (!binding.etPort.equals(null) && !binding.etIPAddress.equals(null)) {
                    val printTest: PrintWifiTest = PrintWifiTest(
                        binding.etIPAddress.text.toString(),
                        binding.etPort.text.toString().toInt(),
                        "B"
                    )
                    printTest()
                } else {
                    Toast.makeText(this, "Debe ingresar la IP y el Puerto", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "exception", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btnSave.setOnClickListener {
            try {
                printers = Printers(1,"B",
                    binding.spDocumentType.selectedItem.toString(),
                    binding.etCopies.text.toString().toInt(),
                    binding.etCharacters.text.toString().toInt(),
                    true,
                    binding.etIPAddress.text.toString(),
                    binding.etPort.text.toString().toInt()
                )
                val printerToSave = printers.createPrinterEntityFromPrinterModel()
                CoroutineScope(Dispatchers.IO).launch {
                    addPrinters(printerToSave)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "exception", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkForm(): Boolean {
        return true
    }
}