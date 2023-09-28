package com.example.printermobile.domain.models

data class Printers(
    private val id: Int,
    private val fontSize: String,
    private val documentType: String,
    private val copyNumber: Int,
    private val charactersNumber: Int,
    private val isUsb: Boolean,
    private val address: String?
)
