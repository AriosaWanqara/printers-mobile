package com.example.printermobile.domain.services

import com.example.printermobile.data.database.repositories.PrinterRepository
import com.example.printermobile.domain.models.Printers
import javax.inject.Inject

class GetAllPrinters @Inject constructor(private val repository: PrinterRepository) {
    suspend fun getAll(): List<Printers> {
        return repository.getAll().map { it -> it.getPrinterModelFromPrinterEntity() }
    }

}