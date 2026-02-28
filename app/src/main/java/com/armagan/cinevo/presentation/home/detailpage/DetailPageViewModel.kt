package com.armagan.cinevo.presentation.home.detailpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.data.model.Review
import com.armagan.cinevo.data.model.VideoResult
import com.armagan.cinevo.data.remote.ApiService
import com.armagan.cinevo.data.repository.AuthRepository
import com.armagan.cinevo.data.repository.MovieFavRepository
import com.armagan.cinevo.data.repository.ReviewRepository
import com.armagan.cinevo.model.MovieDetailResponse
import com.armagan.cinevo.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailPageViewModel @Inject constructor(
    private val apiService: ApiService,
    private val movieFavRepository: MovieFavRepository,
    private val authRepository: AuthRepository,
    private val reviewRepository: ReviewRepository
): ViewModel(){

    private val _movie = MutableStateFlow<MovieDetailResponse?>(null)
    val movie: StateFlow<MovieDetailResponse?> = _movie

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite


    private val _trailers = MutableStateFlow<List<VideoResult>>(emptyList())
    val trailers: StateFlow<List<VideoResult>> = _trailers

    private val _isReviewAdded = MutableStateFlow<Boolean?>(null)
    val isReviewAdded = _isReviewAdded.asStateFlow()

    private val _reviewsByMovie = MutableStateFlow<List<Review>>(emptyList())
    val reviewsByMovie = _reviewsByMovie.asStateFlow()

    private val _userData = MutableStateFlow<Result<User?>>(Result.success(null))
    val userData: StateFlow<Result<User?>> = _userData


    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()

    fun fetchUsername(userId: String) {
        viewModelScope.launch {
            val result = authRepository.getUsernameByUserId(userId)
            _username.value = result
        }
    }

    fun fetchUserData() {
        viewModelScope.launch {
            val result = authRepository.getUserData()
            _userData.value = result
        }
    }


    private suspend fun getMovieVideos(movieId: Int, language: String) {
        try {
            val response = apiService.getMovieVideos(movieId, language)
            val trailerList = response.results.filter {
                it.site == "YouTube" && it.type == "Trailer"
            }

            if (trailerList.isNotEmpty()) {
                _trailers.value = trailerList
            } else {

                val fallbackResponse = apiService.getMovieVideos(movieId, "en-US")
                val fallbackList = fallbackResponse.results.filter {
                    it.site == "YouTube" && it.type == "Trailer"
                }
                _trailers.value = fallbackList
            }
        } catch (e: Exception) {

            try {
                val fallbackResponse = apiService.getMovieVideos(movieId, "en-US")
                val fallbackList = fallbackResponse.results.filter {
                    it.site == "YouTube" && it.type == "Trailer"
                }
                _trailers.value = fallbackList
            } catch (e: Exception) {
                _trailers.value = emptyList()
            }
        }
    }


    fun getMovieDetail(movieId: Int) {
        val language = Locale.getDefault().toLanguageTag()
        viewModelScope.launch {
            try {
                var result = apiService.getMovieDetail(movieId, language)

                if (result.overview.isNullOrEmpty()) {
                    val fallbackResult = apiService.getMovieDetail(movieId, "en-US")
                    result = fallbackResult
                }

                _movie.value = result
                getMovieVideos(movieId, language)
            } catch (e: Exception) {
                try {
                    val fallbackResult = apiService.getMovieDetail(movieId, "en-US")
                    _movie.value = fallbackResult
                    getMovieVideos(movieId, "en-US")
                } catch (e2: Exception) {
                    _movie.value = null
                    _trailers.value = emptyList()
                }
            }
        }
    }

    fun checkIfFavorite(movieId: String) {
        movieFavRepository.isFavorite(movieId) { exists ->
            _isFavorite.value = exists
        }
    }

    fun toggleFavorite(movieId: String) {
        if (_isFavorite.value) {
            movieFavRepository.removeFavorite(movieId)
            _isFavorite.value = false
        } else {
            movieFavRepository.addFavorite(movieId)
            _isFavorite.value = true
        }
    }

    fun submitReview(
        userId: String,
        movieId: String,
        review: String,
        rating: Float,
        isSpoiler: Boolean,
        username:String,
        moviename:String,
        backdroppath:String
    ) {
        viewModelScope.launch {
            val result = reviewRepository.addReview(
                userId = userId,
                movieId = movieId,
                review = review,
                rating = rating,
                isSpoiler = isSpoiler,
                username = username,
                moviename = moviename,
                backdroppath = backdroppath
            )
            _isReviewAdded.value = result
        }
    }

    fun fetchReviewsByMovieId(movieId: String) {
        viewModelScope.launch {
            val reviews = reviewRepository.getReviewsByMovieId(movieId,1,5)
            _reviewsByMovie.value = reviews.toList()
        }
    }
}