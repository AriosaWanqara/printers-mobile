package com.example.printermobile.domain.models

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<BluetoothDomain>>
    val pairedDevices: StateFlow<List<BluetoothDomain>>

    fun startDiscovery()
    fun stopDiscovery()

    fun release()
}