package com.example.printermobile.ui.adapters.bluetooth

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.databinding.ItemBluetoothBinding
import com.example.printermobile.domain.models.BluetoothDomain

class BluetoothViewHolder(view:View) : RecyclerView.ViewHolder(view) {
    val binding = ItemBluetoothBinding.bind(view)
    fun render(bluetoothDomain: BluetoothDomain){
        binding.tvBluetoothName.text = bluetoothDomain.getName() ?: "No name"
    }
}