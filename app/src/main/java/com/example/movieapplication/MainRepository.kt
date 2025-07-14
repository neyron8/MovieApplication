package com.example.movieapplication

import com.example.cinemasearch.network.Cinemas
import com.example.movieapplication.modelsNew.CinemaResponse
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiInterface: ApiInterface) {
    suspend fun getCinemaByName(name: String): Response<Cinemas> {
        return apiInterface.getCinemaByName(name)
    }

    suspend fun getStartMovies(): Response<CinemaResponse>{
        return apiInterface.getStartMovies()
    }
}