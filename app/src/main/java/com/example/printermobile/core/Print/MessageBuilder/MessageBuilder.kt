package com.example.printermobile.core.Print.MessageBuilder

class MessageBuilder {

    private val titleMessage : TitleBuilder
    private val bodyMessage : BodyBuilder
    private val mediaMessage : MediaBuilder

    constructor(titleMessage: TitleBuilder, bodyMessage: BodyBuilder, mediaMessage: MediaBuilder) {
        this.titleMessage = titleMessage
        this.bodyMessage = bodyMessage
        this.mediaMessage = mediaMessage
    }

    fun getTitleMessage():TitleBuilder{
        return this.titleMessage
    }
    fun getBodyMessage():BodyBuilder{
        return this.bodyMessage
    }
    fun getMediaMessage():MediaBuilder{
        return this.mediaMessage
    }
}