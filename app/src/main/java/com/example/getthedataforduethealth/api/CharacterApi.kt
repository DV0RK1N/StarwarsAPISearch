package com.example.getthedataforduethealth.api

import retrofit2.Response
import retrofit2.http.GET

interface CharacterApi {
    /* @GET("project.json")
     suspend fun getCharacters(): Call<List<CharacterDetails>>*/
    @GET("project.json")
    suspend fun getCharacters(): Response<CharactersResult>


}