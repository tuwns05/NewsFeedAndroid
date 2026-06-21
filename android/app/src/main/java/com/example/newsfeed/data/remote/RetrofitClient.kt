package com.example.newsfeed.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
   //private const val BASE_URL = "http://10.0.2.2:5135/"
   private const val BASE_URL = "http://192.168.1.32:5135/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}