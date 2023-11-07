package com.example.printermobile.ui.adapters.Printer

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.core.document.documentType
import com.example.printermobile.core.printType.PrinterType
import com.example.printermobile.domain.models.Printers

class ListPrinterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvCategoryName: TextView = view.findViewById(R.id.tvPrinterName)
    private val tvAddress: TextView = view.findViewById(R.id.tvAddress)
    private val tvName: TextView = view.findViewById(R.id.tvName)
    private val tvCopy: TextView = view.findViewById(R.id.tvCopy)
    private val tvCharacters: TextView = view.findViewById(R.id.tvCharacters)
    private val cvItemContainer: CardView = view.findViewById(R.id.cvItemContainer)
    private val ivType: ImageView = view.findViewById(R.id.ivType)
    private val fbDelete: ImageButton = view.findViewById(R.id.fbDelete)
    fun render(printer: Printers, onItemRedirect: (Printers) -> Unit , onItemRemove: (Printers) -> Unit) {
        val documentType: documentType = documentType()
        tvCategoryName.text =
            documentType.findDocumentByKey(printer.documentType) ?: printer.documentType
        tvName.text = "Nombre: ${printer.name}"
        if (printer.type == PrinterType.WIFI.type) {
            tvAddress.text = "Dirección: ${printer.address}:${printer.port.toString()}"
            ivType.setImageResource(R.drawable.ic_wifi_ic)
        } else if (printer.type == PrinterType.BLUETOOTH.type){
            ivType.setImageResource(R.drawable.ic_bluetooth_ic)
            tvAddress.visibility = View.GONE
        }else if (printer.type == PrinterType.USB.type){
            ivType.setImageResource(R.drawable.ic_usb)
            tvAddress.visibility = View.GONE
        }
        tvCopy.text = "Copias: ${printer.copyNumber.toString()}"
        tvCharacters.text = "Caracteres: ${printer.charactersNumber.toString()}"
        cvItemContainer.setOnClickListener {
            onItemRedirect(printer)
        }
        fbDelete.setOnClickListener {
            onItemRemove(printer)
        }
    }
}