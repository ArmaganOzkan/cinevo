package com.armagan.cinevo.presentation.allreviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.data.model.Review
import com.armagan.cinevo.data.repository.AuthRepository
import com.armagan.cinevo.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllReviewsViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _reviewsByMovie = MutableStateFlow<List<Review>>(emptyList())
    val reviewsByMovie: StateFlow<List<Review>> = _reviewsByMovie.asStateFlow()
    private var currentPage = 1
    private val pageSize = 10
    private var isLastPage = false
    private var isLoading = false
    private val _isLoading2 = MutableStateFlow(false)
    val isLoading2: StateFlow<Boolean> = _isLoading2.asStateFlow()
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()
    fun loadInitialReviews(movieId: String) {
        _isLoading2.value = true
        currentPage = 1
        isLastPage = false
        _reviewsByMovie.value = emptyList()
        fetchReviewsByMovieId(movieId, currentPage)

    }
    fun fetchMoreReviews(movieId: String) {
        if (!isLoading && !isLastPage) {
            currentPage++
            _isLoadingMore.value = true
            fetchReviewsByMovieId(movieId, currentPage)

        }
    }
    private fun fetchReviewsByMovieId(movieId: String, page: Int) {
        viewModelScope.launch {
            isLoading = true
            val reviews = reviewRepository.getReviewsByMovieId(movieId, page, pageSize)
            if (reviews.isEmpty()) {
                isLastPage = true
            } else {
                _reviewsByMovie.value = _reviewsByMovie.value + reviews
            }
            isLoading = false
            _isLoading2.value = false
            _isLoadingMore.value = false
        }
    }
}
