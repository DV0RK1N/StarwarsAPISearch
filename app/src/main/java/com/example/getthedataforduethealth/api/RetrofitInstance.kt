package com.example.getthedataforduethealth.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: CharacterApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://duet-public-content.s3.us-east-2.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharacterApi::class.java)
    }
}