package com.example.printermobile.ui.Views.Printer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.domain.models.Printers

class ListPrinterAdapter(var Printers:List<Printers>):RecyclerView.Adapter<ListPrinterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPrinterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_printer, parent, false)
        return ListPrinterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Printers.size
    }

    override fun onBindViewHolder(holder: ListPrinterViewHolder, position: Int) {
        holder.render(Printers[position])
    }

}