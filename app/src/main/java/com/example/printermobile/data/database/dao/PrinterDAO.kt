package com.example.printermobile.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.printermobile.di.entities.Printers

@Dao
interface PrinterDAO {

    @Query("SELECT * FROM Printers")
    fun getAll(): List<Printers>

    @Query("SELECT * FROM Printers WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Printers>

    @Insert
    fun insertAll(vararg users: Printers)

    @Delete
    fun delete(user: Printers)
}