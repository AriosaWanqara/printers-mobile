package com.example.printermobile.di.repositories

import androidx.annotation.WorkerThread
import com.example.printermobile.di.dao.PrinterDAO
import com.example.printermobile.di.entities.Printers

class PrinterRepository(private val printersDAO: PrinterDAO) {

    @WorkerThread
    suspend fun getPrinters(): List<Printers> {
        return printersDAO.getAll()
    }

    @WorkerThread
    suspend fun addPrinter(printers: Printers) {
        printersDAO.insertAll(printers)
    }
}