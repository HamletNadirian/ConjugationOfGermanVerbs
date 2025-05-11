package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface ConjugationApi {
    @GET("german-verbs-api")
    suspend fun getVerbInfo(@Query("verb") verb: String): Response<ConjugationResponse>
}