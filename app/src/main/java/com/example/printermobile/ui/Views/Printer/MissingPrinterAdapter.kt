package com.example.printermobile.ui.Views.Printer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R

class MissingPrinterAdapter(
    private val documentType: List<String>,
    private val onItemSelected: (String) -> Unit,
) : RecyclerView.Adapter<MissingPrinterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissingPrinterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_document_type, parent, false)
        return MissingPrinterViewHolder(view)
    }

    override fun getItemCount(): Int = documentType.size

    override fun onBindViewHolder(holder: MissingPrinterViewHolder, position: Int) {
        holder.render(documentType[position],onItemSelected)
    }

}