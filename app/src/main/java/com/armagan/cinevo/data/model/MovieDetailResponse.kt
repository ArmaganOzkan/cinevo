package com.armagan.cinevo.model

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String,
    val runtime: Int,
    val genres: List<Genre>,
    val vote_average: Double,
    val vote_count: Int,
)

data class Genre(
    val id: Int,
    val name: String
)