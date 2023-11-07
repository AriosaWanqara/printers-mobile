package com.example.printermobile.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.printermobile.domain.models.BluetoothDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDomain(): BluetoothDomain {
    return BluetoothDomain(
        name = name,
        address = address
    )
}