package com.example.cinemasearch.network


import com.google.gson.annotations.SerializedName

data class Cinemas(
    @SerializedName("films")
    val films: List<Film>,
    @SerializedName("keyword")
    val keyword: String?,
    @SerializedName("pagesCount")
    val pagesCount: Int?,
    @SerializedName("searchFilmsCountResult")
    val searchFilmsCountResult: Int?
) {
    data class Film(
        @SerializedName("countries")
        val countries: List<Country>,
        @SerializedName("description")
        val description: String?,
        @SerializedName("filmId")
        val filmId: Int,
        @SerializedName("filmLength")
        val filmLength: String?,
        @SerializedName("genres")
        val genres: List<Genre?>?,
        @SerializedName("nameEn")
        val nameEn: String?,
        @SerializedName("nameRu")
        val nameRu: String?,
        @SerializedName("posterUrl")
        val posterUrl: String?,
        @SerializedName("posterUrlPreview")
        val posterUrlPreview: String?,
        @SerializedName("rating")
        val rating: String?,
        @SerializedName("ratingVoteCount")
        val ratingVoteCount: Int?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("year")
        val year: String?
    ) {
        data class Country(
            @SerializedName("country")
            val country: String?
        )

        data class Genre(
            @SerializedName("genre")
            val genre: String?
        )
    }
}