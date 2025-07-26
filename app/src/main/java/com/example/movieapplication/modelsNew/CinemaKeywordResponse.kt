package com.example.movieapplication.modelsNew


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CinemaKeywordResponse(
    @SerialName("films")
    val films: List<Film>,
    @SerialName("keyword")
    val keyword: String?,
    @SerialName("pagesCount")
    val pagesCount: Int?,
    @SerialName("searchFilmsCountResult")
    val searchFilmsCountResult: Int?
)
{
    data class Film(
        @SerialName("countries")
        val countries: List<Country?>?,
        @SerialName("description")
        val description: String?,
        @SerialName("filmId")
        val filmId: Int?,
        @SerialName("filmLength")
        val filmLength: String?,
        @SerialName("genres")
        val genres: List<Genre?>?,
        @SerialName("nameEn")
        val nameEn: String?,
        @SerialName("nameRu")
        val nameRu: String?,
        @SerialName("posterUrl")
        val posterUrl: String?,
        @SerialName("posterUrlPreview")
        val posterUrlPreview: String?,
        @SerialName("rating")
        val rating: String?,
        @SerialName("ratingVoteCount")
        val ratingVoteCount: Int?,
        @SerialName("type")
        val type: String?,
        @SerialName("year")
        val year: String?
    )
}