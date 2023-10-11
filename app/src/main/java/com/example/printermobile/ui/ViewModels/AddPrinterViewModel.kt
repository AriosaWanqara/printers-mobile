package com.example.printermobile.ui.ViewModels

import androidx.lifecycle.ViewModel
import com.example.printermobile.data.database.entities.Printers
import com.example.printermobile.domain.services.AddPrinters
import com.example.printermobile.domain.services.GetAllPrinters
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPrinterViewModel @Inject constructor(
    private val addPrinters: AddPrinters,
    private val getAllPrinters: GetAllPrinters,
) : ViewModel() {
    suspend fun onAdd(printers: Printers) {
        addPrinters(printers)
    }
    suspend fun getAll():List<com.example.printermobile.domain.models.Printers>{
        return getAllPrinters.getAll()
    }
}