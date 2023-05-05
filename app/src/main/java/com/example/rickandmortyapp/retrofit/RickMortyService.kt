package com.example.rickandmortyapp.retrofit

import com.example.rickandmortyapp.retrofit.RickMortyService.rickMortyApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RickMortyService {
    val BASE_URL = "https://rickandmortyapi.com/api/"
    val rickMortyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RickMortyApi::class.java)
}
   fun call() = rickMortyApi.getRickMortyData()


