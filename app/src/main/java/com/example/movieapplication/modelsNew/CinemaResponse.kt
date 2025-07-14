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
) {
    data class Item(
        @SerialName("countries")
        val countries: List<Country?>?,
        @SerialName("genres")
        val genres: List<Genre?>?,
        @SerialName("imdbId")
        val imdbId: String?,
        @SerialName("kinopoiskId")
        val kinopoiskId: Int?,
        @SerialName("nameEn")
        val nameEn: Any?,
        @SerialName("nameOriginal")
        val nameOriginal: String?,
        @SerialName("nameRu")
        val nameRu: String?,
        @SerialName("posterUrl")
        val posterUrl: String?,
        @SerialName("posterUrlPreview")
        val posterUrlPreview: String?,
        @SerialName("ratingImdb")
        val ratingImdb: Double?,
        @SerialName("ratingKinopoisk")
        val ratingKinopoisk: Double?,
        @SerialName("type")
        val type: String?,
        @SerialName("year")
        val year: Int?
    ) {
        data class Country(
            @SerialName("country")
            val country: String?
        )

        data class Genre(
            @SerialName("genre")
            val genre: String?
        )
    }
}