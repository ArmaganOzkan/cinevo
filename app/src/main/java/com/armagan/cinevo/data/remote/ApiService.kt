package com.armagan.cinevo.data.remote

import com.armagan.cinevo.data.model.MovieResponse
import com.armagan.cinevo.data.model.VideoResponse
import com.armagan.cinevo.model.MovieDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("language") language: String
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): MovieDetailResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): VideoResponse
}