package com.example.printermobile.ui.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.domain.services.DeletePrinter
import com.example.printermobile.domain.services.GetAllPrinters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListPrintersViewModel @Inject constructor(
    private val getAllPrinters: GetAllPrinters,
    private val deletePrinter: DeletePrinter
) : ViewModel() {

    var printers = MutableLiveData<List<Printers>>()
    fun onCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getAllPrinters.getAll()
                if (result.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        printers.value = mutableListOf()
                        printers.value = result
                    }

                }

            } catch (e: Exception) {
                println(e)
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
}