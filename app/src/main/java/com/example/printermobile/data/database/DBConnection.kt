package com.example.printermobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.printermobile.data.database.dao.PrinterDAO
import com.example.printermobile.data.database.dao.SystemTypeDAO
import com.example.printermobile.data.database.entities.Printers
import com.example.printermobile.data.database.entities.SystemTypeEntity
import dagger.Provides


    @Database(entities = [Printers::class, SystemTypeEntity::class], version = 3)
abstract class DBConnection : RoomDatabase() {
    abstract fun printersDAO(): PrinterDAO
    abstract fun systemTypeDAO(): SystemTypeDAO
}