package com.example.printermobile.ui.Views.advance

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.printermobile.core.print.test.PrintBluetoothTest
import com.example.printermobile.core.print.test.PrintUSBTest
import com.example.printermobile.core.print.test.PrintWifiTest
import com.example.printermobile.core.printType.PrinterType
import com.example.printermobile.data.database.entities.PrintersEntity
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.domain.services.AddPrinters
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdvancePrinterViewModel @Inject constructor(
    private val addPrinters: AddPrinters,
) : ViewModel() {
    var printerType = MutableLiveData<String>(null)
    var port = MutableLiveData<String>(null)
    var ipAddress = MutableLiveData<String>(null)
    var name = MutableLiveData<String>(null)
    var copyNumber = MutableLiveData<String>(null)
    var charactersNumber = MutableLiveData<String>(null)
    var documentType = MutableLiveData<List<String>>(listOf())

    fun printTest(context: Context) {
        try {
            if (printerType.value == PrinterType.WIFI.type) {
                if (!port.value.isNullOrEmpty() && !ipAddress.value.isNullOrEmpty()) {
                    PrintWifiTest(
                        ipAddress.value!!.trim(),
                        port.value!!.toInt(),
                        "B"
                    )()
                } else {
                    Toast.makeText(context, "Debe ingresar la IP y el Puerto", Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (printerType.value == PrinterType.BLUETOOTH.type) {
                if (!PrintBluetoothTest(context)()) {
                    Toast.makeText(context, "Impresora no vinculada", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (!PrintUSBTest(context)()) {
                    Toast.makeText(context, "Impresora no conectada", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "exception", Toast.LENGTH_SHORT)
                .show()
        }
    }

    suspend fun onAdd() {
        try {
            for (documents in documentType.value!!) {
                val printers = Printers(
                    null,
                    "B",
                    name.value!!,
                    documents,
                    copyNumber.value!!.toInt(),
                    charactersNumber.value!!.toInt(),
                    printerType.value!!,
                    ipAddress.value,
                    null
                )
                if (!port.value.isNullOrEmpty()) {
                    printers.port = port.value!!.toInt()
                }
                val printerToSave = printers.createPrinterEntityFromPrinterModel()
                addPrinters(printerToSave)
            }
        }catch (e:Exception){
            println(e)
        }
    }
}