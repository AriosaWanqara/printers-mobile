package com.example.printermobile.ui.adapters.faq

import androidx.recyclerview.widget.DiffUtil
import com.example.printermobile.domain.models.FaQ
import com.example.printermobile.domain.models.Printers


class HelperPrinterDiffUtils(
    private val oldList: List<FaQ>,
    private val newList: List<FaQ>
) :DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getId() == newList[newItemPosition].getId()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getId() == newList[newItemPosition].getId()
    }

}