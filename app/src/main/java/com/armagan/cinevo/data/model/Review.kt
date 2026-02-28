package com.armagan.cinevo.data.model

data class Review(
    val id: String = "",
    val createdAt: String? = null,
    val userId: String = "",
    val movieId: String = "",
    val review: String = "",
    val rating: Float = 0f,
    val isSpoiler: Boolean = false,
    val username: String = "",
    val moviename: String = "",
    val backdroppath: String = ""
)