package com.example.movieapplication.repository

import com.example.movieapplication.modelsNew.CinemaKeywordResponse
import com.example.movieapplication.modelsNew.CinemaResponse
import com.example.movieapplication.modelsNew.FilmData
import com.example.movieapplication.network.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiInterface: ApiInterface) {
    suspend fun getCinemaByName(name: String): Response<CinemaKeywordResponse> {
        return apiInterface.getCinemaByName(name)
    }

    suspend fun getStartMovies(): Response<CinemaResponse> {
        return apiInterface.getStartMovies()
    }

    suspend fun getDataById(id: Int): Response<FilmData>{
        return apiInterface.getDataById(id)
    }
}