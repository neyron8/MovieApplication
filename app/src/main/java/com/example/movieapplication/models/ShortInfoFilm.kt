package com.example.cinemasearch.network

import android.os.Parcelable


data class ShortInfoFilm(
    val id: Int,
    val nameEn: String?,
    val rating: String?,
    val description: String?,
    val filmLength: String?,
    val posterUrl: String?
)
