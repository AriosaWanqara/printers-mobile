package com.example.printermobile.domain.models

class Printers {
    val id: Int
    val fontSize: String
    val documentType: String
    val copyNumber: Int
    val charactersNumber: Int
    val isWifi: Boolean
    val address: String?

    constructor(
        id: Int,
        fontSize: String,
        documentType: String,
        copyNumber: Int,
        charactersNumber: Int,
        isWifi: Boolean,
        address: String?
    ) {
        this.id = id
        this.fontSize = fontSize
        this.documentType = documentType
        this.copyNumber = copyNumber
        this.charactersNumber = charactersNumber
        this.isWifi = isWifi
        this.address = address
    }

}
