package com.example.printermobile.ui.Views.Printer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.domain.models.Printers

class ListPrinterAdapter(
    var printers: List<Printers>,
    private val onItemRedirect: (Printers) -> Unit,
    private val onItemRemove: (Printers) -> Unit,
) : RecyclerView.Adapter<ListPrinterViewHolder>() {

    fun updateList(newList: List<Printers>){
        val listPrintersDiffUtils = ListPrintersDiffUtils(printers,newList)
        val result = DiffUtil.calculateDiff(listPrintersDiffUtils)
        printers = newList
        result.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPrinterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_printer, parent, false)
        return ListPrinterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return printers.size
    }

    override fun onBindViewHolder(holder: ListPrinterViewHolder, position: Int) {
        holder.render(printers[position],onItemRedirect,onItemRemove)
    }

}