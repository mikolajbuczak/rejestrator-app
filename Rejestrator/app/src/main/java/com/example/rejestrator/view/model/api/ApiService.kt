package com.example.rejestrator.view.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL="http://192.168.1.240:80/rejestrator/api/"

object ApiService {
    private val retrofit by lazy{

        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }

    val api: RejestratorApi by lazy {
        retrofit
                .create(RejestratorApi::class.java)
    }

}