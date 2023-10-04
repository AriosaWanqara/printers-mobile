package com.example.printermobile.domain.services

import com.example.printermobile.domain.models.Printers

class GetAllPrinters {
    fun getAll(): MutableList<Printers> {
        return mutableListOf(
            Printers(1, "B", "Recibos", 1, 64, true, "192.168.100.70",9100),
            Printers(2, "B", "Factura electronica", 1, 32, false, null,null),
            Printers(3, "B", "Factura Preimpresa", 1, 32, false, null,null),
            Printers(4, "B", "Comanda de cocina", 1, 32, true, "192.168.100.70",9100),
            Printers(5, "B", "Comanda de barra", 1, 32, false, null,null),
            Printers(6, "B", "Preticket", 1, 32, true, "192.168.100.70",9100),
            Printers(7, "B", "Cotizacion detallada", 1, 32, false, null,null),
            Printers(8, "B", "Cotizacion resumida", 1, 32, true, "192.168.100.70",9100),
            Printers(9, "B", "Gaveta de dinero", 1, 32, true, "192.168.100.70",9100),
            Printers(10, "B", "Cierre de gaveta", 1, 32, true, "192.168.100.70",9100),
        )
    }


}