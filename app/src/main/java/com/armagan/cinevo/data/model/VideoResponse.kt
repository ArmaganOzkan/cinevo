package com.armagan.cinevo.data.model

data class VideoResponse(
    val id: Int,
    val results: List<VideoResult>
)

data class VideoResult(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    val official: Boolean
)
