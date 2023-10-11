package com.example.printermobile.domain.services

import com.example.printermobile.data.database.repositories.PrinterRepository
import com.example.printermobile.domain.models.Printers
import javax.inject.Inject

class GetPrinter @Inject constructor(private val printerRepository: PrinterRepository) {

    suspend operator fun invoke(id:Int): Printers {
        return printerRepository.findPrinterById(id).getPrinterModelFromPrinterEntity()
    }
    suspend fun getPrinterByDocument(document:String): Printers? {
        return printerRepository.findPrinterByDocumentType(document)?.getPrinterModelFromPrinterEntity()
    }
}