package com.example.printermobile.ui.ViewModels

import androidx.lifecycle.ViewModel
import com.example.printermobile.data.database.entities.Printers
import com.example.printermobile.domain.services.AddPrinters
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPrinterViewModel @Inject constructor(
    private val addPrinters: AddPrinters
) : ViewModel() {
    suspend fun onAdd(printers: Printers) {
        addPrinters(printers)
    }
}