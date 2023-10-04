package com.example.printermobile.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.printermobile.data.database.entities.Printers

@Dao
interface PrinterDAO {

    @Query("SELECT * FROM Printers")
    fun getAll(): List<Printers>
    @Query("SELECT * FROM Printers WHERE document_type IN (:documentType)")
    fun getPrinterByDocumentType(documentType:String): Printers?

    @Query("SELECT * FROM Printers WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Printers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: Printers)

    @Delete
    fun delete(user: Printers)
}