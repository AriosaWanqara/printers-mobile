package com.example.printermobile.domain.services

import com.example.printermobile.data.database.dao.PrinterDAO
import com.example.printermobile.data.database.entities.Printers
import javax.inject.Inject

class AddPrinters @Inject constructor(private val repository: PrinterDAO) {
    suspend operator fun invoke(printers: Printers){
        repository.insertAll(printers)
    }
}