package com.example.printermobile.core.document

class documentType(){
    private val documentMap : Map<String,String> = mapOf(
        "R" to "Recibos",
        "FE" to "Factura electronica"
    )

    fun getDocuments():MutableList<String>{
        var list:MutableList<String> = mutableListOf()
        this.documentMap.map { document ->
             list.add(document.value)
        }
        return list
    }

}
