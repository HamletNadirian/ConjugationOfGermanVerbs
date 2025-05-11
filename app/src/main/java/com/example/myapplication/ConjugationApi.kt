package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET
<<<<<<< HEAD
=======
import retrofit2.http.Path
>>>>>>> c68c2041f628ac64f5685efdb7367cd87119a351
import retrofit2.http.Query

interface ConjugationApi {
    @GET("german-verbs-api")
    suspend fun getVerbInfo(@Query("verb") verb: String): Response<ConjugationResponse>
}