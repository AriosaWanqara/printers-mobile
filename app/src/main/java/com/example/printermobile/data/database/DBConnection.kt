package com.example.printermobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.printermobile.data.database.dao.PrinterDAO
import com.example.printermobile.data.database.entities.Printers
import dagger.Provides


@Database(entities = [Printers::class], version = 2)
abstract class DBConnection : RoomDatabase() {
    abstract fun printersDAO(): PrinterDAO
}