package com.example.printermobile.core.print.messageBuilder

class BodyBuilder {

    private val bodyMessage:List<String>

    constructor(bodyMessage: List<String>) {
        this.bodyMessage = bodyMessage
    }

    public fun getBodyMessage():List<String>{
        return this.bodyMessage
    }
}