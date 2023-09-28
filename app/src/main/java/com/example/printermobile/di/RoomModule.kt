package com.example.printermobile.di

import android.content.Context
import androidx.room.Room
import com.example.printermobile.data.database.DBConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, DBConnection::class.java, "printer_database")

    @Singleton
    @Provides
    fun providePrinterDAO(database:DBConnection) = database.printersDAO()
}