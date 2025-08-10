package com.example.movieapplication.network

import com.example.movieapplication.modelsNew.CinemaKeywordResponse
import com.example.movieapplication.modelsNew.CinemaStartResponse
import com.example.movieapplication.modelsNew.FilmData
import com.example.movieapplication.modelsNew.ScreenShots
import com.example.movieapplication.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("v2.2/films/premieres?year=2025&month=FEBRUARY")
    @Headers(
        "X-API-KEY: ${Constants.KEY}",
        "Content-Type: application/json",
    )
    suspend fun getStartMovies(

    ): Response<CinemaStartResponse>

    @GET("v2.1/films/search-by-keyword")
    @Headers(
        "X-API-KEY: ${Constants.KEY}",
        "Content-Type: application/json",
    )
    suspend fun getCinemaByName(
        @Query("keyword") keyword:String
    ): Response<CinemaKeywordResponse>

    @GET("v2.2/films/{id}")
    @Headers(
        "X-API-KEY: ${Constants.KEY}",
        "Content-Type: application/json",
    )
    suspend fun getDataById(
        @Path("id")id: Int
    ):Response<FilmData>

    @GET("v2.2/films/{id}/images?page=1")
    @Headers(
        "X-API-KEY: ${Constants.KEY}",
        "Content-Type: application/json",
    )
    suspend fun getScreenShotsById(
        @Path("id")id: Int,
        @Query("type")type: String
    ):Response<ScreenShots>
}