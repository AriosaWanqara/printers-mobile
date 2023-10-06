package com.example.printermobile.ui.Views.Printer

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.core.document.documentType
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.Views.UpdatePrinterActivity

class ListPrinterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvCategoryName: TextView = view.findViewById(R.id.tvPrinterName)
    private val tvAddress: TextView = view.findViewById(R.id.tvAddress)
    private val tvPort: TextView = view.findViewById(R.id.tvPort)
    private val tvCopy: TextView = view.findViewById(R.id.tvCopy)
    private val tvCharacters: TextView = view.findViewById(R.id.tvCharacters)
    private val cvItemContainer: CardView = view.findViewById(R.id.cvItemContainer)
    fun render(printer: Printers, onItemRedirect: (Printers) -> Unit) {
        val documentType: documentType = documentType()
        tvCategoryName.text =
            documentType.findDocumentByKey(printer.documentType) ?: printer.documentType
        if (printer.isWifi) {
            tvAddress.text = printer.address
            tvPort.text = printer.port.toString()
        } else {
            tvAddress.visibility = View.GONE
            tvPort.visibility = View.GONE
        }
        tvCopy.text = printer.copyNumber.toString()
        tvCharacters.text = printer.charactersNumber.toString()
        cvItemContainer.setOnClickListener {
            onItemRedirect(printer)
        }
    }
}