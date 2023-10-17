package com.example.printermobile.data.network

import com.example.printermobile.data.model.reponse.FaQApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface FaQApiClient {
    @GET("support/faqs")
    suspend fun getAllFaQ(): Response<FaQApiResponse>
}