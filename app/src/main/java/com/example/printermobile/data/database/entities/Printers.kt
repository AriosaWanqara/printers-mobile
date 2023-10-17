package com.example.printermobile.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.printermobile.domain.models.Printers

@Entity
data class Printers(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "font_size")
    var fontSize: String,

    @ColumnInfo(name = "document_type")
    var documentType: String,

    @ColumnInfo(name = "copy_number")
    var copyNumber: Int,

    @ColumnInfo(name = "characters_number")
    var charactersNumber: Int,

    @ColumnInfo(name = "is_usb")
    var isUsb: Boolean,

    @ColumnInfo(name = "address")
    var address: String?,

    @ColumnInfo(name = "port")
    var port: Int?,
) {
    fun getPrinterModelFromPrinterEntity(): Printers {
        return Printers(
            this.id,
            this.name,
            this.fontSize,
            this.documentType,
            this.copyNumber,
            this.charactersNumber,
            this.isUsb,
            this.address,
            this.port
        )
    }
}
