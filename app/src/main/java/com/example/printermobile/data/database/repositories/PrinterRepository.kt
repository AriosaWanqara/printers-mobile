package com.example.printermobile.data.database.repositories

import com.example.printermobile.data.database.dao.PrinterDAO
import com.example.printermobile.data.database.entities.Printers
import javax.inject.Inject

class PrinterRepository @Inject constructor(
    private val printerDAO: PrinterDAO
) {
    suspend fun add(printers: Printers) {
        val printersList = printerDAO.getAll()
        if (printersList.isNotEmpty()){
            printersList.forEach lit@{ it ->
                if (it.documentType == printers.documentType){
                    printers.id = it.id
                    return@lit
                }
            }
        }
        printerDAO.insertAll(printers)
    }

    suspend fun getAll(): List<Printers> {
        return printerDAO.getAll()
    }
}