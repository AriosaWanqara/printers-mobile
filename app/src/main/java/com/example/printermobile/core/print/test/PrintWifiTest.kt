package com.example.printermobile.core.print.test

import com.example.printermobile.core.print.EscposCoffee
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.output.TcpIpOutputStream


class PrintWifiTest(private var host: String, private var port: Int, private var fontType: String) {
    fun invoke() {
        TcpIpOutputStream(host, port).use { outputStream ->
            val style = Style()
            if (this.fontType == "A") {
                style.setFontName(Style.FontName.Font_A_Default)
            } else {
                style.setFontName(Style.FontName.Font_B)
            }
            val escposCoffee = EscposCoffee(style, outputStream)
            escposCoffee.printWifiTest(host,port,fontType)
        }
    }
}