package com.example.movieapplication.modelsNew

import kotlinx.serialization.SerialName

data class Country(
    @SerialName("country")
    val country: String?
)

data class Genre(
    @SerialName("genre")
    val genre: String?
)
