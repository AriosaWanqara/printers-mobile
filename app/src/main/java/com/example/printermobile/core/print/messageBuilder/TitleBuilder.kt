package com.example.printermobile.core.print.messageBuilder

class TitleBuilder {

    private val titleMessage: List<String>

    constructor(titleMessage: List<String>) {
        this.titleMessage = titleMessage
    }

    fun getTitleMessage(): List<String> {
        return this.titleMessage
    }
}