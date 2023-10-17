package com.example.printermobile.data.model.reponse

import com.example.printermobile.data.model.FaQNetworkModel
import com.google.gson.annotations.SerializedName

data class FaQApiResponse(
    @SerializedName("data") private val faQNetworkModel: List<FaQNetworkModel>
) {
    fun getFaQNetworkModel(): List<FaQNetworkModel> {
        return this.faQNetworkModel
    }
}
