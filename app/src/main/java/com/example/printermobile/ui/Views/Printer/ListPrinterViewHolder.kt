package com.example.printermobile.ui.Views.Printer

import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.core.document.documentType
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.Views.UpdatePrinterActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        if (printer.isWifi) {
            tvAddress.text = "Dirección: ${printer.address}:${printer.port.toString()}"
            ivType.setImageResource(R.drawable.ic_wifi_ic)
        } else {
            ivType.setImageResource(R.drawable.ic_bluetooth_ic)
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