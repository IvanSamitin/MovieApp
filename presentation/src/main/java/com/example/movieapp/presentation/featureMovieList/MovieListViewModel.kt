package com.example.movieapp.presentation.featureMovieList

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

class MovieListViewModel(
    private val movieRepository: MovieRepository,
    private val movieCategory: MovieCategory,
) : ViewModel() {
    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MovieListState())
    val state =
        _state
            .onStart {
                if (!hasLoadedInitialData) {
                    getCollection(movieCategory)
                    hasLoadedInitialData = true
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = MovieListState(),
            )

    private fun getCollection(movieCategory: MovieCategory) {
        viewModelScope.launch {
            _state.value =
                _state.value.copy(
                    isRefreshing = true,
                )
            val response = movieRepository.getCollection(type = movieCategory, 1)
            when (response) {
                is Result.Error -> {
                    val errorMessage =
                        when (response.error) {
                            DataError.Network.NO_INTERNET -> "Отсутсвует интернет"
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
                            movieDetails = response.data,
                            loading = false,
                            error = null,
                        )
                }
            }
            _state.value =
                _state.value.copy(
                    isRefreshing = false,
                )
        }
    }

    fun onAction(action: MovieListAction) {
        when (action) {
            MovieListAction.ActionUpdate -> getCollection(movieCategory)
            else -> Unit
        }
    }
}
