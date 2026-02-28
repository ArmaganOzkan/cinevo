package com.armagan.cinevo.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val results:List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val genre_ids: List<Int>,
    val vote_average: Double,
    @SerializedName("backdrop_path") val backdropPath: String?,
    )