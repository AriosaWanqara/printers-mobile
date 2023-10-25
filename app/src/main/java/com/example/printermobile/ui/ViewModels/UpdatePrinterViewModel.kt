package com.example.printermobile.ui.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.domain.services.AddPrinters
import com.example.printermobile.domain.services.GetPrinter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UpdatePrinterViewModel @Inject constructor(
    private val getPrinter: GetPrinter,
    private val addPrinters: AddPrinters
) : ViewModel() {

    var printer = MutableLiveData<Printers>()

    fun onCreate(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getPrinter(id)
                println(result)
                withContext(Dispatchers.Main) {
                    printer.value = result
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }
    suspend fun onAdd(printersEntity: com.example.printermobile.data.database.entities.PrintersEntity) {
        addPrinters(printersEntity)
    }
}