package com.example.printermobile.ui.adapters.bluetooth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.domain.models.BluetoothDomain

class BluetoothAdapter(
    private var bluetoothDomainList: List<BluetoothDomain>,
    private val onItemClick: (BluetoothDomain) -> Unit,
) : RecyclerView.Adapter<BluetoothViewHolder>() {

    fun updateList(newList: List<BluetoothDomain>){
        val listPrintersDiffUtils = BluetoothDiffUtils(bluetoothDomainList,newList)
        val result = DiffUtil.calculateDiff(listPrintersDiffUtils)
        bluetoothDomainList = newList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bluetooth, parent, false)
        return BluetoothViewHolder(view)
    }

    override fun getItemCount(): Int = bluetoothDomainList.size

    override fun onBindViewHolder(holder: BluetoothViewHolder, position: Int) {
        holder.render(bluetoothDomainList[position])
        holder.itemView.setOnClickListener {
            onItemClick(bluetoothDomainList[position])
        }
    }
}