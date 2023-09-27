package com.example.printermobile.core.Print

import com.example.printermobile.core.Print.MessageBuilder.BodyBuilder
import com.example.printermobile.core.Print.MessageBuilder.MediaBuilder
import com.example.printermobile.core.Print.MessageBuilder.MessageBuilder
import com.example.printermobile.core.Print.MessageBuilder.TitleBuilder

interface PrinterLibraryRepository {

    fun print(messageBuilder: MessageBuilder)

    fun printTitle(titleBuilder: TitleBuilder)

    fun printBody(bodyBuilder: BodyBuilder)

    fun printMedia(mediaBuilder: MediaBuilder)

    fun cut()

    fun printTest()

    fun openCashDrawer()
}