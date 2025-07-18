package com.example.movieapplication.utils

import com.example.movieapplication.network.ApiInterface
import com.example.movieapplication.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DiModules {

    @Singleton
    @Provides
    fun provideApiService(): ApiInterface {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiInterface::class.java)
    }


    @Provides
    fun provideMainRepository(apiInterface: ApiInterface): MainRepository {
        return MainRepository(apiInterface = apiInterface)
    }

}