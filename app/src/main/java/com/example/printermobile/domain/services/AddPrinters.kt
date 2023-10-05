package com.example.printermobile.domain.services

import com.example.printermobile.data.database.entities.Printers
import com.example.printermobile.data.database.repositories.PrinterRepository
import javax.inject.Inject


class AddPrinters @Inject constructor(private val repository: PrinterRepository) {
    suspend operator fun invoke(printers: Printers){
        repository.add(printers)
    }
}