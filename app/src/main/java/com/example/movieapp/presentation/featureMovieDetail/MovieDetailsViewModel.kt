package com.example.movieapp.presentation.featureMovieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.result_logic.DataError
import com.example.movieapp.domain.result_logic.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository,
    private val movieId: Int
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MovieDetailsState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                getMovieDetails(movieId)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MovieDetailsState()
        )

    fun onAction(action: MovieDetailsAction) {
        when (action) {
            is MovieDetailsAction.Update -> getMovieDetails(movieId)
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val response = movieRepository.getMovieDetails(movieId)
            when (response) {
                is Result.Error -> {
                    val errorMessage = when (response.error) {
                        DataError.Network.NO_INTERNET -> "Отсутсвует интернет"
                    }
                    _state.value = _state.value.copy(
                        loading = false,
                        error = errorMessage
                    )
                }

                is Result.Success -> {
                    _state.value = _state.value.copy(
                        movieDetails = response.data,
                        loading = false,
                        error = null
                    )
                }
            }
        }
    }
}