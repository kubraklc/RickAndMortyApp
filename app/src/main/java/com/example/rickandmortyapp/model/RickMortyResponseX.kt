package com.example.rickandmortyapp.model

import com.google.gson.annotations.SerializedName

data class RickMortyResponseX(
    val info: Info,
    val results: List<Result>
)


