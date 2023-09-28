package com.example.printermobile.di.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Printers(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "font_size")
    val fontSize: String,

    @ColumnInfo(name = "document_type")
    val documentType: String,

    @ColumnInfo(name = "copy_number")
    val copyNumber: Int,

    @ColumnInfo(name = "characters_number")
    val charactersNumber: Int,

    @ColumnInfo(name = "is_usb")
    val isUsb: Boolean,

    @ColumnInfo(name = "address")
    val address: String?,
)
