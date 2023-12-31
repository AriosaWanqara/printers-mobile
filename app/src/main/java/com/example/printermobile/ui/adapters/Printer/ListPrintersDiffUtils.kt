package com.example.printermobile.ui.adapters.Printer

import androidx.recyclerview.widget.DiffUtil
import com.example.printermobile.domain.models.Printers

class ListPrintersDiffUtils(
    private val oldList: List<Printers>,
    private val newList: List<Printers>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
}