package com.armagan.cinevo.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.armagan.cinevo.data.model.Movie
import com.armagan.cinevo.data.remote.ApiService
import com.armagan.cinevo.data.repository.MovieFavRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


sealed class SearchUiState {
    object Loading : SearchUiState()
    object Searching : SearchUiState()
    object Empty : SearchUiState()
    data class Success(val movies: List<Movie>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}


@HiltViewModel
class SearchPageViewModel @Inject constructor(
    private val apiService: ApiService,
    private val movieFavRepository: MovieFavRepository

):ViewModel(){

    private var currentPage = 1
    private var isPaginating = false

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    val language = Locale.getDefault().toLanguageTag()

    private val _genreMoviesMap = MutableStateFlow<Map<Int, List<Movie>>>(emptyMap())
    val genreMoviesMap: StateFlow<Map<Int, List<Movie>>> = _genreMoviesMap

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState


    val searchQuery = MutableStateFlow("")
    init {
        observeQueryChanges()
    }

    fun setLoading() {
        _uiState.value = SearchUiState.Loading
    }

    var isFilterMode = false
        private set

    fun setFilterMode(enabled: Boolean) {
        isFilterMode = enabled
    }


    private fun observeQueryChanges() {
        viewModelScope.launch {
            searchQuery
                .debounce(400)
                .collect { query ->
                    if (query.isNotBlank()) {
                        searchMoviesByQuery(query)
                    } else {
                        if(!isFilterMode){
                            resetPageAndList()
                        }
                    }
                }
        }
    }
    fun resetPageAndList() {
        currentPage = 1
        isPaginating = false
        _uiState.value = SearchUiState.Loading
        fetchNextPage()
    }



    fun fetchNextPage() {
        if(isPaginating || isFilterMode) return
        viewModelScope.launch {
            isPaginating=true
            if(currentPage ==1){
                _uiState.value = SearchUiState.Loading
            }

            try {
                val response = apiService.getPopularMovies(
                    language,
                    currentPage
                )

                val currentList = if (_uiState.value is SearchUiState.Success) {
                    (_uiState.value as SearchUiState.Success).movies
                } else {
                    emptyList()
                }
                val newList = currentList + response.results

                _uiState.value = if (newList.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(newList)
                }


                currentPage++
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error("Veriler alınamadı")
            }finally {
                isPaginating=false
            }
        }
    }


    fun searchMoviesByQuery(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Searching
            try {
                currentPage = 1
                val response = apiService.searchMovies(
                    query = query,
                    language = language,
                    page = currentPage
                )



                _uiState.value = if (response.results.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(response.results)
                }

            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error("Arama başarısız")
            }
        }
    }


    fun fetchMoviesByGenre(genreId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = SearchUiState.Loading

                val response = apiService.getMoviesByGenre(genreId,language)

                _uiState.value = if (response.results.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(response.results)
                }

            } catch (e: Exception) {
                _errorMessage.value = "Kategoriye göre veriler alınamadı: ${e.message}"
                _uiState.value = SearchUiState.Error("Filtreleme sırasında hata oluştu.")
            }
        }
    }
}