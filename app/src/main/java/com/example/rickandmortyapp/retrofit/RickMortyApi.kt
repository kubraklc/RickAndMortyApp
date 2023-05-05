package com.example.rickandmortyapp.retrofit

import com.example.rickandmortyapp.model.RickMortyDetailResponseX
import com.example.rickandmortyapp.model.RickMortyResponseX


import retrofit2.http.GET
import retrofit2.http.Path

interface RickMortyApi {
    @GET("character")
    fun getRickMortyData():retrofit2.Call<RickMortyResponseX>

    @GET("character/{id}")
    fun getCharacterDetail(@Path("id") id: String?): retrofit2.Call<RickMortyDetailResponseX>
}

