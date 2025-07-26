package com.example.movieapplication.modelsNew

import kotlinx.serialization.SerialName

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
    val nameEn: String?,
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

}