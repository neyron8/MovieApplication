package com.example.movieapplication.modelsNew


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CinemaResponse(
    @SerialName("items")
    val items: List<Item>,
    @SerialName("total")
    val total: Int?,
    @SerialName("totalPages")
    val totalPages: Int?
)