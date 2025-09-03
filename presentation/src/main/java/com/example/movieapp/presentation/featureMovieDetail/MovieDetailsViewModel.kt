package com.example.movieapp.presentation.featureMovieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.Result
import com.example.movieapp.presentation.util.NavigationChannel
import com.example.movieapp.presentation.util.NavigationEvent
import com.example.movieapp.presentation.util.asErrorUiText
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
                        _state.value =
                            _state.value.copy(
                                loading = false,
                                error = result.asErrorUiText(),
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

    private fun updateSheet() {
        _state.value = state.value.copy(
            isSheetOpen = !state.value.isSheetOpen
        )
    }

    private fun sendNavigationEvent(event: NavigationEvent) {
        viewModelScope.launch {
            NavigationChannel.sendEvent(event = event)
        }
    }

    fun onAction(action: MovieDetailsAction) {
        when (action) {
            is MovieDetailsAction.Update -> getMovieDetails(movieId)
            is MovieDetailsAction.AddToFav -> addToFavorite(movieId)
            is MovieDetailsAction.SheetUpdate -> updateSheet()
            is MovieDetailsAction.NavigateBack -> sendNavigationEvent(
                NavigationEvent.OnNavigateBack
            )

            is MovieDetailsAction.SeasonOverview -> sendNavigationEvent(
                NavigationEvent.OnSeasonOverviewClick(
                    action.movieId
                )
            )
        }
    }


}
