package com.example.movieapplication.modelsNew


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenShots(
    @SerialName("items")
    val items: List<ItemX>?,
    @SerialName("total")
    val total: Int?,
    @SerialName("totalPages")
    val totalPages: Int?
) {
    data class ItemX(
        @SerialName("imageUrl")
        val imageUrl: String?,
        @SerialName("previewUrl")
        val previewUrl: String?
    )
}
