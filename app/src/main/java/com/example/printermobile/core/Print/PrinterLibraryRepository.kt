package com.example.printermobile.core.Print

import com.example.printermobile.core.Print.MessageBuilder.BodyBuilder
import com.example.printermobile.core.Print.MessageBuilder.MediaBuilder
import com.example.printermobile.core.Print.MessageBuilder.MessageBuilder
import com.example.printermobile.core.Print.MessageBuilder.TitleBuilder

interface PrinterLibraryRepository {

    suspend fun print(messageBuilder: MessageBuilder)

    fun printTitle(titleBuilder: TitleBuilder)

    fun printBody(bodyBuilder: BodyBuilder)

    suspend fun printMedia(mediaBuilder: MediaBuilder)

    fun cut()

    fun printTest()

    fun openCashDrawer()
}