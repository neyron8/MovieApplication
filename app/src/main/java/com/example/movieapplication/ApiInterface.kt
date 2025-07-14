package com.example.movieapplication

import com.example.cinemasearch.network.Cinemas
import com.example.movieapplication.modelsNew.CinemaResponse
import com.example.movieapplication.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @GET("films?order=RATING&type=ALL&ratingFrom=0&ratingTo=10&yearFrom=1000&yearTo=3000&page=1")
    @Headers(
        "X-API-KEY: ${Constants.KEY}",
        "Content-Type: application/json",
    )
    suspend fun getStartMovies(

    ): Response<CinemaResponse> //Respomse - разберись что это

    @GET("films/search-by-keyword")
    @Headers(
        "X-API-KEY: ${Constants.KEY}",
        "Content-Type: application/json",
    )
    suspend fun getCinemaByName(
        @Query("keyword") keyword:String
    ): Response<Cinemas>
}