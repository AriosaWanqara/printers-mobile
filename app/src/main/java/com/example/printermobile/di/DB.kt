package com.example.printermobile.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.printermobile.di.entities.Printers
import com.example.printermobile.di.dao.PrinterDAO

@Database(entities = [Printers::class], version = 1)
abstract class DB : RoomDatabase() {
    abstract fun printersDAO(): PrinterDAO

    companion object {
        @Volatile
        private var instance: DB? = null

        fun getDBInstance(context: Context): DB? {
            return instance ?: synchronized(this)
            {
                val volatileInstance = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java,
                    "mobile-database"
                ).build()

                instance = volatileInstance
                instance
            }
        }
    }
}