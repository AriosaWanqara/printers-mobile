package com.example.printermobile.core.Print.MessageBuilder

class TitleBuilder {

    private val titleMessage: List<String>

    constructor(titleMessage: List<String>) {
        this.titleMessage = titleMessage
    }

    fun getTitleMessage(): List<String> {
        return this.titleMessage
    }
}