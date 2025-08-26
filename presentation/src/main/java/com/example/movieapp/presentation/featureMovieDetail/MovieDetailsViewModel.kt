package com.example.movieapp.presentation.featureMovieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository,
    private val movieId: Int,
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MovieDetailsState())
    val state =
        _state
            .onStart {
                if (!hasLoadedInitialData) {
                    getMovieDetails(movieId)
                    hasLoadedInitialData = true
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = MovieDetailsState(),
            )

    private fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getMovieDetails(movieId).collect { result ->


                when (result) {
                    is Result.Error -> {
                        val errorMessage =
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> "Отсутсвует интернет"
                                DataError.Network.SERVER_ERROR -> "Ошибка сервера"
                            }
                        _state.value =
                            _state.value.copy(
                                loading = false,
                                error = errorMessage,
                            )
                    }

                    is Result.Success -> {
                        _state.value =
                            _state.value.copy(
                                movieDetails = result.data.movie,
                                isFavorite = result.data.isInFavorites,
                                loading = false,
                                error = null,
                            )
                    }
                }
            }
        }
    }

    private fun addToFavorite(id: Int) {
        viewModelScope.launch {
            if (state.value.isFavorite) {
                movieRepository.deleteFromList(movieId = id, category = MovieCategory.FAVORITE)
            } else {
                movieRepository.addToList(movieId = id, category = MovieCategory.FAVORITE)
            }
        }
    }

    fun onAction(action: MovieDetailsAction) {
        when (action) {
            is MovieDetailsAction.Update -> getMovieDetails(movieId)
            is MovieDetailsAction.AddToFav -> addToFavorite(movieId)
        }
    }
}
