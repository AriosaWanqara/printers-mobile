package com.example.printermobile.domain.models

data class Printers(
    val id: Int,
    val fontSize: String,
    val documentType: String,
    val copyNumber: Int,
    val charactersNumber: Int,
    val isWifi: Boolean,
    val address: String?
)
