package com.example.printermobile.core.Print.MessageBuilder

class BodyBuilder {

    private val bodyMessage:List<String>

    constructor(bodyMessage: List<String>) {
        this.bodyMessage = bodyMessage
    }

    public fun getBodyMessage():List<String>{
        return this.bodyMessage
    }
}