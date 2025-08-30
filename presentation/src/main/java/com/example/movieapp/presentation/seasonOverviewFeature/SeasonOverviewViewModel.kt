package com.example.movieapp.presentation.seasonOverviewFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class SeasonOverviewViewModel(
    private val movieRepository: MovieRepository,
    private val movieId: Int,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SeasonOverviewState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SeasonOverviewState()
        )

    fun onAction(action: SeasonOverviewAction) {
        when (action) {
            else -> {}
        }
    }

}