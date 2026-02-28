package com.armagan.cinevo.data.remote

import com.armagan.cinevo.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetroFitInstance {
    private const val BASE_URL = "https://api.themoviedb.org/3/"


    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(BuildConfig.TMDB_API_KEY))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()


    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}