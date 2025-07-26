package com.example.movieapplication.utils

import com.example.movieapplication.modelsNew.CinemaKeywordResponse
import com.example.movieapplication.modelsNew.Item
import java.time.Year

fun convertFilmToItem(film: List<CinemaKeywordResponse.Film>): List<Item> {
        val listOfResult = mutableListOf<Item>()
        film.forEach {
             listOfResult.add(Item(
                countries = it.countries,
                genres = it.genres,
                imdbId = it.filmId.toString(),
                kinopoiskId = it.filmId,
                nameEn = it.nameEn,
                nameOriginal = it.nameRu,
                nameRu = it.nameEn,
                posterUrl = it.posterUrl,
                posterUrlPreview = it.posterUrlPreview,
                ratingImdb = 0.0,
                ratingKinopoisk = ratingConv(it.rating.toString()),
                type = it.type,
                year = yearConv(it.year.toString())
            ))

        }
        return listOfResult
}

fun ratingConv(rating: String): Double{
    if (rating == "null") {
        return 0.0
    }
    return rating.toDouble()
}

fun yearConv(year: String): Int{
    if (year == "null"){
        return 0
    }
    return year.toInt()
}
