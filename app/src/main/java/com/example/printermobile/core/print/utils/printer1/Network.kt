package com.example.printermobile.core.print.utils.printer1

import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException

class Network {
    operator fun invoke(
        p_dato: String,
        p_servicio: String,
        p_ambiente: String
    ): JSONObject? {
        return try {
            val uri = "http://$p_ambiente$p_servicio$p_dato"
            val json = Jsoup.connect(uri).ignoreContentType(true).execute().body()
            JSONObject(json)
        } catch (e: Exception) {
            println(e)
            null
        }
    }
}