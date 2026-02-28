package com.armagan.cinevo.presentation.home.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.data.model.Movie
import com.armagan.cinevo.data.remote.ApiService
import com.armagan.cinevo.util.GenreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService:ApiService
):ViewModel(){
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _genreMoviesMap = MutableStateFlow<Map<Int, List<Movie>>>(emptyMap())
    val genreMoviesMap: StateFlow<Map<Int, List<Movie>>> = _genreMoviesMap

    private val _randomGenres = MutableStateFlow<List<Int>>(emptyList())
    val randomGenres: StateFlow<List<Int>> = _randomGenres

    init {
        fetchPopularMovies()
        generateRandomGenres()

        _randomGenres.value.forEach {genreId ->
            fetchMoviesByGenre(genreId)
        }
    }

    private fun generateRandomGenres() {
        _randomGenres.value = GenreUtil
            .getAllGenreIds()
            .shuffled()
            .take(3)
    }


    private fun fetchPopularMovies() {
        val language = Locale.getDefault().toLanguageTag()

        viewModelScope.launch {
            try {
                val response = apiService.getPopularMovies(language,1)
                _movies.value = response.results
            } catch (e: Exception) {
                _errorMessage.value = "Veriler alınamadı: ${e.message}"
            }
        }
    }

    fun fetchMoviesByGenre(genreId: Int) {
        val language = Locale.getDefault().toLanguageTag()
        viewModelScope.launch {
            try {
                val response = apiService.getMoviesByGenre(genreId,language)
                val currentMap = _genreMoviesMap.value.toMutableMap()
                currentMap[genreId] = response.results
                _genreMoviesMap.value = currentMap
            } catch (e: Exception) {
                _errorMessage.value = "Kategoriye göre veriler alınamadı: ${e.message}"
            }
        }
    }

}