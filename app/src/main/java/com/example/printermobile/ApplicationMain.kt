package com.example.printermobile

import android.app.Application
import com.example.printermobile.di.DB
import com.example.printermobile.di.repositories.PrinterRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationMain:Application() {
    private val database by lazy { DB.getDBInstance(this) }
    private val printerRepository by lazy { PrinterRepository(database!!.printersDAO()) }
}