package com.example.printermobile.ui.Views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.printermobile.core.print.utils.printer1.Discrimination
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        CoroutineScope(Dispatchers.IO).launch {
            print()
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
               withContext(Dispatchers.Main){
                   binding.llPrintGift.visibility = View.GONE
                   binding.mcMissingPrintersWarningContainer.visibility = View.VISIBLE
               }

            }
        } catch (e: InterruptedException) {
            Toast.makeText(this, "Error al imprimir", Toast.LENGTH_SHORT).show()
            result = false
            return false
        } finally {
            if (result) {
                moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        return true
    }
}