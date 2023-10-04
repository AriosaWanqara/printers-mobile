package com.example.printermobile.domain.models

import com.example.printermobile.data.database.entities.Printers

class Printers {
    val id: Int?
    val fontSize: String
    val documentType: String
    val copyNumber: Int
    val charactersNumber: Int
    val isWifi: Boolean
    val address: String?
    val port: Int?

    constructor(
        id: Int?,
        fontSize: String,
        documentType: String,
        copyNumber: Int,
        charactersNumber: Int,
        isWifi: Boolean,
        address: String?,
        port: Int?
    ) {
        this.id = id
        this.fontSize = fontSize
        this.documentType = documentType
        this.copyNumber = copyNumber
        this.charactersNumber = charactersNumber
        this.isWifi = isWifi
        this.address = address
        this.port = port
    }

    fun createPrinterEntityFromPrinterModel(): Printers {
        return Printers(
            0,
            this.fontSize,
            this.documentType,
            this.copyNumber,
            this.charactersNumber,
            this.isWifi,
            this.address,
            this.port
        )
    }
}
