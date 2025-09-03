package com.example.movieapp.presentation.seasonOverviewFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import com.example.movieapp.presentation.util.NavigationChannel
import com.example.movieapp.presentation.util.NavigationEvent
import com.example.movieapp.presentation.util.asErrorUiText
import com.example.movieapp.presentation.util.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SeasonOverviewViewModel(
    private val movieRepository: MovieRepository,
    private val movieId: Int,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SeasonOverviewState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                getSeasonOverview(movieId)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SeasonOverviewState()
        )

    private fun getSeasonOverview(movieId: Int) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                loading = true
            )

            val response = movieRepository.getSeasonOverview(movieId)
            when (response) {
                is Result.Error -> {
                    _state.value = state.value.copy(
                        loading = false,
                        error = response.asErrorUiText()
                    )
                }

                is Result.Success -> _state.value =
                    state.value.copy(season = response.data, loading = false, error = null)
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            NavigationChannel.sendEvent(NavigationEvent.OnNavigateBack)
        }
    }

    fun onAction(action: SeasonOverviewAction) {
        when (action) {
            is SeasonOverviewAction.NavigateBack -> navigateBack()
            is SeasonOverviewAction.Retry -> getSeasonOverview(movieId)
        }
    }

}