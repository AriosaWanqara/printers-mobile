package com.example.printermobile.ui.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.domain.models.SystemType
import com.example.printermobile.domain.services.AddPrinters
import com.example.printermobile.domain.services.AddSystemType
import com.example.printermobile.domain.services.DeletePrinter
import com.example.printermobile.domain.services.GetAllPrinters
import com.example.printermobile.domain.services.GetSystemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListPrintersViewModel @Inject constructor(
    private val getAllPrinters: GetAllPrinters,
    private val addPrinter: AddPrinters,
    private val deletePrinter: DeletePrinter,
    private val getSystemType: GetSystemType,
    private val addSystemType: AddSystemType
) : ViewModel() {

    var printers = MutableLiveData<List<Printers>>(listOf())
    var systemType = MutableLiveData<SystemType?>()
    var isLoading = MutableLiveData(false)
    fun onCreate() {
        isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getAllPrinters.getAll()
                val systemTypeResult = getSystemType(1)
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                    printers.value = result
                    systemType.value = systemTypeResult
                }
            } catch (e: Exception) {
                println(e)
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                }
            }
        }
    }

    suspend fun onDeletePrinter(id: Int) {
        try {
            deletePrinter(id)
            withContext(Dispatchers.Main) {
                printers.value = printers.value!!.filter { it.id != id }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    suspend fun onAddSystemType(system: SystemType) {
        addSystemType(system)
        withContext(Dispatchers.Main) {
            systemType.value = system
        }
    }

    suspend fun onAdd(printer: com.example.printermobile.data.database.entities.PrintersEntity) {
        try {
            addPrinter(printer)
            withContext(Dispatchers.Main) {
                printers.value = printers.value!!.plus(printer.getPrinterModelFromPrinterEntity())
            }
        } catch (e: Exception) {
            println(e)
        }
    }
}