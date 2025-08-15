package com.example.movieapp.presentation.featureHome

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

class HomeViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                getCollection(movieCategory = MovieCategory.TOP_POPULAR_ALL)
                getCollection(movieCategory = MovieCategory.TOP_250_MOVIES)
                getCollection(movieCategory = MovieCategory.TOP_250_TV_SHOWS)
                getCollection(movieCategory = MovieCategory.COMICS_THEME)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )


    private fun getCollection(movieCategory: MovieCategory) {
        viewModelScope.launch {
            val response = movieRepository.getCollection(type = movieCategory, 1)
            when (response) {
                is Result.Error -> {
                    val errorMessage =
                        when (response.error) {
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
                            movieCollection = _state.value.movieCollection + (movieCategory to response.data),
                            loading = false,
                            error = null,
                        )
                }
            }

        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            else -> Unit
        }
    }

}