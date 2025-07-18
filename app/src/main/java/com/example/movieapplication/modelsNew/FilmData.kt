package com.example.movieapplication.modelsNew


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilmData(
    @SerialName("completed")
    val completed: Boolean? = true,
    @SerialName("countries")
    val countries: List<Country?>? = emptyList(),
    @SerialName("coverUrl")
    val coverUrl: String? = "",
    @SerialName("description")
    val description: String? = "",
    @SerialName("editorAnnotation")
    val editorAnnotation: String? = "",
    @SerialName("endYear")
    val endYear: Int? = 0,
    @SerialName("filmLength")
    val filmLength: Int? = 0,
    @SerialName("genres")
    val genres: List<Genre?>? = emptyList(),
    @SerialName("has3D")
    val has3D: Boolean? = true,
    @SerialName("hasImax")
    val hasImax: Boolean? = true,
    @SerialName("imdbId")
    val imdbId: String? = "",
    @SerialName("isTicketsAvailable")
    val isTicketsAvailable: Boolean? = true,
    @SerialName("kinopoiskHDId")
    val kinopoiskHDId: String? = "",
    @SerialName("kinopoiskId")
    val kinopoiskId: Int? = 0,
    @SerialName("lastSync")
    val lastSync: String? = "",
    @SerialName("logoUrl")
    val logoUrl: String? = "",
    @SerialName("nameEn")
    val nameEn: String? = "",
    @SerialName("nameOriginal")
    val nameOriginal: String? = "",
    @SerialName("nameRu")
    val nameRu: String? = "",
    @SerialName("posterUrl")
    val posterUrl: String? = "",
    @SerialName("posterUrlPreview")
    val posterUrlPreview: String? = "",
    @SerialName("productionStatus")
    val productionStatus: String? = "",
    @SerialName("ratingAgeLimits")
    val ratingAgeLimits: String? = "",
    @SerialName("ratingAwait")
    val ratingAwait: Double? = 0.0,
    @SerialName("ratingAwaitCount")
    val ratingAwaitCount: Int? = 0,
    @SerialName("ratingFilmCritics")
    val ratingFilmCritics: Double? = 0.0,
    @SerialName("ratingFilmCriticsVoteCount")
    val ratingFilmCriticsVoteCount: Int? = 0,
    @SerialName("ratingGoodReview")
    val ratingGoodReview: Double? = 0.0,
    @SerialName("ratingGoodReviewVoteCount")
    val ratingGoodReviewVoteCount: Int? = 0,
    @SerialName("ratingImdb")
    val ratingImdb: Double? = 0.0,
    @SerialName("ratingImdbVoteCount")
    val ratingImdbVoteCount: Int? = 0,
    @SerialName("ratingKinopoisk")
    val ratingKinopoisk: Double? = 0.0,
    @SerialName("ratingKinopoiskVoteCount")
    val ratingKinopoiskVoteCount: Int? = 0,
    @SerialName("ratingMpaa")
    val ratingMpaa: String? = "",
    @SerialName("ratingRfCritics")
    val ratingRfCritics: Double? = 0.0,
    @SerialName("ratingRfCriticsVoteCount")
    val ratingRfCriticsVoteCount: Int?= 0,
    @SerialName("reviewsCount")
    val reviewsCount: Int? = 0,
    @SerialName("serial")
    val serial: Boolean? = true,
    @SerialName("shortDescription")
    val shortDescription: String? = "",
    @SerialName("shortFilm")
    val shortFilm: Boolean? =true,
    @SerialName("slogan")
    val slogan: String? ="",
    @SerialName("startYear")
    val startYear: Int? = 0,
    @SerialName("type")
    val type: String? = "",
    @SerialName("webUrl")
    val webUrl: String? = "",
    @SerialName("year")
    val year: Int? = 0
){
    data class Genre(
        @SerialName("genre")
        val genre: String?
    )

    data class Country(
        @SerialName("country")
        val country: String?
    )
}