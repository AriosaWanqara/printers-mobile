package com.example.printermobile.core.Print

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.printermobile.core.Print.Imp.BitmapCoffeeImage
import com.example.printermobile.core.Print.MessageBuilder.BodyBuilder
import com.example.printermobile.core.Print.MessageBuilder.MediaBuilder
import com.example.printermobile.core.Print.MessageBuilder.MessageBuilder
import com.example.printermobile.core.Print.MessageBuilder.TitleBuilder
import com.github.anastaciocintra.escpos.EscPos
import com.github.anastaciocintra.escpos.EscPosConst
import com.github.anastaciocintra.escpos.Style
import com.github.anastaciocintra.escpos.barcode.BarCode
import com.github.anastaciocintra.escpos.barcode.QRCode
import com.github.anastaciocintra.escpos.image.Bitonal
import com.github.anastaciocintra.escpos.image.BitonalOrderedDither
import com.github.anastaciocintra.escpos.image.EscPosImage
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper
import java.io.IOException
import java.io.OutputStream
import java.net.URL


class EscposCoffee : PrinterLibraryRepository {

    private var style: Style
    private val outputStream: OutputStream

    constructor(style: Style, outputStream: OutputStream) {
        this.style = style
        this.outputStream = outputStream
    }

    fun setStyle(style: Style) {
        this.style = style
    }

    override fun print(messageBuilder: MessageBuilder) {
        try {
            this.printTitle(messageBuilder.getTitleMessage())
            this.printBody(messageBuilder.getBodyMessage())
            this.printMedia(messageBuilder.getMediaMessage())
            this.cut()
        } catch (e: Exception) {

        }
    }

    override fun printTitle(titleBuilder: TitleBuilder) {
        val escPos: EscPos = EscPos(this.outputStream)
        try {
            titleBuilder.getTitleMessage().forEach { message ->
                escPos.write(style, message)
            }
            escPos.close()
        } catch (e: Exception) {

        }
    }

    override fun printBody(bodyBuilder: BodyBuilder) {
        val escPos: EscPos = EscPos(this.outputStream)
        try {
            bodyBuilder.getBodyMessage().forEach { message ->
                escPos.write(this.style, message)
            }
            escPos.close()
        } catch (e: Exception) {
            println(e)
        }
    }

    override fun printMedia(mediaBuilder: MediaBuilder) {
        val escPos: EscPos = EscPos(this.outputStream)
        try {
            mediaBuilder.getMediaMessage().forEach { (key, message) ->
                if (key == "imgU") {
                    message.forEach { image ->
                        printImageFromUrl(escPos, image)
                    }
                }
                if (key == "BC") {
                    message.forEach { barCode ->
                        printBarCode(escPos, barCode)
                    }
                }
                if (key == "QR") {
                    message.forEach { qr ->
                        printQRCode(escPos, qr)
                    }
                }
            }
            escPos.close()
        } catch (e: Exception) {

        }
    }

    override fun cut() {
        try {
            val escpos = EscPos(this.outputStream)
            escpos.feed(4)
            escpos.cut(EscPos.CutMode.FULL)
            escpos.close()
        } catch (ex: IOException) {
            println(ex.message)
        }
    }

    override fun printTest() {
         try {
//            val escpos = EscPos(this.outputStream)
//            val title = Style()
//                .setFontSize(Style.FontSize._2, Style.FontSize._2)
//                .setJustification(EscPosConst.Justification.Center)
//            val title1 = Style()
//                .setLineSpacing(8)
//                .setFontName(Style.FontName.Font_B)
//            val characters = arrayOfNulls<String>(11)
//            Arrays.fill(characters, "000000000-")
//            val zeros = java.lang.String.join("", *characters)
//            escpos.writeLF(title, "Test")
//            val pms = PrintModeStyle().setFontSize(false, false).setFontName(config.getFontName())
//            escpos.feed(1)
//            escpos.writeLF(pms, zeros)
//            escpos.feed(1)
//            escpos.writeLF(
//                title1,
//                "Cuente los caracteres que entran en la primera linea de texto y coloque el valor en Cantidad de caracteres 👌"
//            )
//            escpos.feed(1)
//            escpos.writeLF(
//                Style().setJustification(EscPosConst.Justification.Center),
//                "Configuracion Actual"
//            )
//            escpos.writeLF("Nombre de impresora: " + config.getPrinterName())
//            escpos.writeLF("Tipo de fuente: " + config.getFontName().name())
//            escpos.feed(2)
//            escpos.feed(5)
//            escpos.cut(EscPos.CutMode.FULL)
//            escpos.close()
        } catch (ex: IOException) {
        }
    }

    override fun openCashDrawer() {
        try {
            val escpos = EscPos(this.outputStream)
            escpos.write(27).write(112).write(0).write(25).write(250)
            escpos.close()
        } catch (e: java.lang.Exception) {
            println(e.message)
        }
    }

    private fun printImageFromUrl(escpos: EscPos, imageUrl: String) {
        try {
            val url = URL(imageUrl)
            val image:Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            val algorithm: Bitonal = BitonalOrderedDither()
            val imageWrapper = RasterBitImageWrapper()
            imageWrapper.setJustification(EscPosConst.Justification.Center)
            val escposImage = EscPosImage(BitmapCoffeeImage(image), algorithm)
            escpos.write(imageWrapper, escposImage)
            escpos.feed(2)
        } catch (e: java.lang.Exception) {
            println(e.message)
        }
    }

    private fun printQRCode(escpos: EscPos, qrCodeMessage: String) {
        try {
            val qrcode = QRCode()
            qrcode.setJustification(EscPosConst.Justification.Center)
            escpos.feed(1)
            escpos.write(qrcode, qrCodeMessage)
            escpos.feed(1)
        } catch (e: java.lang.Exception) {
            println(e.message)
        }
    }

    private fun printBarCode(escpos: EscPos, barCodeMessage: String) {
        try {
            val barcode = BarCode()
            escpos.feed(1)
            escpos.write(barcode, barCodeMessage)
            escpos.feed(1)
        } catch (e: java.lang.Exception) {
            println(e.message)
        }
    }

}