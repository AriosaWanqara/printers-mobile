package com.example.printermobile.ui.adapters.bluetooth

import androidx.recyclerview.widget.DiffUtil
import com.example.printermobile.domain.models.BluetoothDomain

class BluetoothDiffUtils(
    private val newList:List<BluetoothDomain>,
    private val oldList:List<BluetoothDomain>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getAddress() == newList[newItemPosition].getAddress()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getAddress() == newList[newItemPosition].getAddress()
    }
}