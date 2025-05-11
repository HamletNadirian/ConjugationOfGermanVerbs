package com.example.myapplication

<<<<<<< HEAD

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
=======
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
>>>>>>> c68c2041f628ac64f5685efdb7367cd87119a351

object RetrofitInstance {
    private const val BASE_URL = "https://german-verbs-api.onrender.com/"

<<<<<<< HEAD
    // Настраиваем OkHttpClient с таймаутами
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
=======
    private fun getInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
>>>>>>> c68c2041f628ac64f5685efdb7367cd87119a351
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

<<<<<<< HEAD
    val dictionaryApi: ConjugationApi = getInstance().create(ConjugationApi::class.java)
=======
    val dictionaryApi : ConjugationApi = getInstance().create(ConjugationApi::class.java)


>>>>>>> c68c2041f628ac64f5685efdb7367cd87119a351
}