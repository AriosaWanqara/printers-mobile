package com.example.printermobile.data.network.repositories

import com.example.printermobile.data.network.FaQApiClient
import com.example.printermobile.domain.models.FaQ
import javax.inject.Inject

class FaQRepository @Inject constructor(private val api:FaQApiClient) {
    suspend operator fun invoke(): List<FaQ> {
        val body = api.getAllFaQ().body()
        return body?.getFaQNetworkModel()?.map { it.createFaQFromFaQNetworkModel() } ?: listOf()
    }
}