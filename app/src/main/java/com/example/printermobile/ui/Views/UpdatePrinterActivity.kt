package com.example.printermobile.ui.Views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.printermobile.R
import com.example.printermobile.core.document.documentType
import com.example.printermobile.core.print.test.PrintWifiTest
import com.example.printermobile.databinding.ActivityUpdatePrinterBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.ViewModels.ListPrintersViewModel
import com.example.printermobile.ui.ViewModels.UpdatePrinterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdatePrinterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePrinterBinding
    private val updatePrinterViewModel: UpdatePrinterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var id = intent.getStringExtra("printer")?.let {
                updatePrinterViewModel.onCreate(it.toInt())
            }
        var ids = intent.getStringExtra("printers")
        println(ids)


        hideSystemUI()
        initUI()
        initListeners()

//        id?.let { updatePrinterViewModel.onCreate(it.to) }

        updatePrinterViewModel.printer.observe(this, Observer {
            initData(it)
        })

    }

    private fun initData(printer: Printers) {
        binding.etName.setText(printer.name)
        binding.etCharacters.setText(printer.charactersNumber.toString())
        binding.etCopies.setText(printer.copyNumber.toString())
        printer.port?.let { binding.etPort.setText(it.toString()) }
        printer.address?.let { binding.etIPAddress.setText(it) }
    }

    private fun initUI() {
        val documentType: documentType = documentType()
        val documentTypeSpinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            documentType.getDocuments()
        )
        val fontTypeSpinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("B", "A")
        )
        binding.spDocumentType.adapter = documentTypeSpinnerAdapter
        binding.spFontType.adapter = fontTypeSpinnerAdapter
    }

    private fun initListeners() {
        binding.btnPrintTest.setOnClickListener {
            try {
                if (!binding.etPort.equals(null) && !binding.etIPAddress.equals(null)) {
                    if (binding.tbPrinterType.isChecked) {
                        PrintWifiTest(
                            binding.etIPAddress.text.toString().trim(),
                            binding.etPort.text.toString().toInt(),
                            "B"
                        )()
                    } else {

                    }
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
                var printers = Printers(
                    updatePrinterViewModel.printer.value?.id,
                    binding.etName.text.toString(),
                    binding.spFontType.selectedItem.toString().trim(),
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
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        updatePrinterViewModel.onAdd(printerToSave)
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

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}