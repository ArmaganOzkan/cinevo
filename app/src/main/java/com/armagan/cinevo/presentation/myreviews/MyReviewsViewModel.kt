package com.armagan.cinevo.presentation.myreviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.data.model.Review
import com.armagan.cinevo.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReviewsViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
):ViewModel(){


    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private var recentlyRemovedReview: Review? = null


    fun removeFromList(review: Review) {
        recentlyRemovedReview = review
        _reviews.update { list ->
            list.filter { it.id != review.id }
        }
    }

    fun restoreReview() {
        recentlyRemovedReview?.let { review ->
            _reviews.update { list ->
                list + review
            }
            recentlyRemovedReview = null
        }
    }

    fun confirmDelete() {
        recentlyRemovedReview?.let { review ->
            viewModelScope.launch {
                reviewRepository.deleteReviewById(review.id)
                recentlyRemovedReview = null
            }
        }
    }



    fun loadReviewsByUserId(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = reviewRepository.getReviewsByUserId(userId)
                _reviews.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}

