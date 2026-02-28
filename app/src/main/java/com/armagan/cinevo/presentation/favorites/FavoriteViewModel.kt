package com.armagan.cinevo.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.BuildConfig
import com.armagan.cinevo.data.remote.ApiService
import com.armagan.cinevo.data.repository.MovieFavRepository
import com.armagan.cinevo.model.MovieDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val movieFavRepository: MovieFavRepository,
    private val apiService: ApiService
) : ViewModel() {

    private val _favoriteMovies = MutableStateFlow<List<MovieDetailResponse>>(emptyList())
    val favoriteMovies = _favoriteMovies.asStateFlow()

    private val _favoriteMovieIds = MutableStateFlow<List<String>>(emptyList())
    val favoriteMovieIds = _favoriteMovieIds.asStateFlow()

    init {
        loadFavoriteMoviesId()
    }

    private fun loadFavoriteMoviesId() {
        viewModelScope.launch {
            movieFavRepository.getFavoriteMovieIds { ids ->
                _favoriteMovieIds.value = ids
                fetchMovieInfos(ids)
            }
        }
    }

    private fun fetchMovieInfos(ids: List<String>) {
        val language = Locale.getDefault().toLanguageTag()
        viewModelScope.launch {
            val resultList = ids.mapNotNull { id ->
                try {
                    val movieIdInt = id.toIntOrNull()
                    if (movieIdInt != null) {
                        apiService.getMovieDetail(movieIdInt, language)
                    } else null
                } catch (e: Exception) {
                    null
                }
            }
            _favoriteMovies.value = resultList
        }
    }


    fun removeFavorite2(movieId: String) {
        viewModelScope.launch {
            movieFavRepository.removeFavorite(movieId)
            _favoriteMovies.update { it.filterNot { movie -> movie.id.toString() == movieId } }
        }
    }
    fun addFavorite(movie: MovieDetailResponse){
        viewModelScope.launch {
            movieFavRepository.addFavorite(movie.id.toString())

            _favoriteMovies.update { currentList ->
                currentList + movie
            }
        }
    }
}