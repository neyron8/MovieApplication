package com.example.movieapplication.modelsNew


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    @SerialName("items")
    val items: List<Item>? = emptyList<Item>(),
    @SerialName("total")
    val total: Int? = 0
){
    data class Item(
        @SerialName("name")
        val name: String? = "null",
        @SerialName("site")
        val site: String? = "null",
        @SerialName("url")
        val url: String? = "null"
    )
}