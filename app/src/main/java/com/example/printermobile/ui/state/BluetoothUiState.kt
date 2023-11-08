package com.example.printermobile.ui.state

import com.example.printermobile.domain.models.BluetoothDomain

data class BluetoothUiState (
    val scannedDevices: List<BluetoothDomain> = emptyList(),
    val pairedDevices: List<BluetoothDomain> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
)