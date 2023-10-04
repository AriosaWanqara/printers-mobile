package com.example.printermobile.core.document

class documentType(){
    private val documentMap : Map<String,String> = mapOf(
        "R" to "Recibos",
        "FE" to "Factura electronica",
        "PT" to "Pretickets",
        "CC" to "Comandas cocina",
        "CB" to "Comandas barra",
        "CO" to "Comandas otros",
        "CD" to "Cotizaciones detalladas",
        "CR" to "Cotizaciones resumidas",
        "GD" to "Gaveta de dinero",
        "CDC" to "Cierre de caja"
    )

    fun getDocuments():MutableList<String>{
        val list:MutableList<String> = mutableListOf()
        this.documentMap.map { document ->
             list.add(document.value)
        }
        return list
    }
    fun findDocumentByKey(key:String): String? {
        return this.documentMap[key]
    }
    fun findKeyByDocument(document:String): String? {
        return this.documentMap.filterValues { it == document }.keys.toString()
    }
}
