package com.example.printermobile.ui.ViewModels

import androidx.lifecycle.ViewModel
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.domain.models.SystemType
import com.example.printermobile.domain.services.GetAllPrinters
import com.example.printermobile.domain.services.GetPrinter
import com.example.printermobile.domain.services.GetSystemType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BackgroundPrinterViewModel @Inject constructor(
    private val getPrinter: GetPrinter,
    private val getAllPrinters: GetAllPrinters,
    private val getSystemType: GetSystemType,
) :ViewModel() {

    suspend fun onCreate(document:String): Printers? {
        return getPrinter.getPrinterByDocument(document)
    }

    suspend fun getAll():List<Printers>{
        return getAllPrinters.getAll()
    }

    suspend fun getSystemType(): SystemType? {
        return getSystemType(1)
    }
}