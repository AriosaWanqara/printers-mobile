package com.example.printermobile.domain.services

import com.example.printermobile.data.database.repositories.PrinterRepository
import javax.inject.Inject

class DeletePrinter  @Inject constructor(private val repository: PrinterRepository) {

    suspend operator fun invoke(printers: Int){
        repository.delete(printers)
    }
}