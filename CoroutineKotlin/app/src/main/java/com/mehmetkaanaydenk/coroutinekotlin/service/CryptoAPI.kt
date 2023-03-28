package com.mehmetkaanaydenk.coroutinekotlin.service

import com.mehmetkaanaydenk.coroutinekotlin.model.Crypto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CryptoAPI {

    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    suspend fun getAll(): Response<List<Crypto>>

}