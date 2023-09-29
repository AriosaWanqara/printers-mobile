package com.example.printermobile.core.print.test

import com.example.printermobile.core.print.EscposCoffee
import com.github.anastaciocintra.escpos.Style
import java.io.OutputStream

class PrintBluetoothTest(private var fontType:String,private var outputStream: OutputStream) {
    fun invoke(){
        val style = Style()
        val escposCoffee = EscposCoffee(style, this.outputStream)
        escposCoffee.printBluetoothTest(this.fontType)
    }
}