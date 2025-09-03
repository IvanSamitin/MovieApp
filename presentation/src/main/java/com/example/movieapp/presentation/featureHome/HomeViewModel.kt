package com.example.movieapp.presentation.featureHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import com.example.movieapp.presentation.util.ConnectivityObserver
import com.example.movieapp.presentation.util.NavigationChannel
import com.example.movieapp.presentation.util.NavigationEvent
import com.example.movieapp.presentation.util.asErrorUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadData()
                isConncted()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    private fun loadData(){
        getCollection(movieCategory = MovieCategory.TOP_POPULAR_ALL)
        getCollection(movieCategory = MovieCategory.TOP_250_MOVIES)
        getCollection(movieCategory = MovieCategory.TOP_250_TV_SHOWS)
        getCollection(movieCategory = MovieCategory.COMICS_THEME)
    }

    private fun getCollection(movieCategory: MovieCategory) {
        viewModelScope.launch {
            movieRepository.getCollection(type = movieCategory, 1).collect{ result ->
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
                                movieCollection = _state.value.movieCollection + (movieCategory to result.data),
                                loading = false,
                                error = null,
                            )
                    }
                }
            }
        }
    }

    private suspend fun isConncted() {
        viewModelScope.launch {
            connectivityObserver.isConnected.collect {
                _state.value = _state.value.copy(
                    isConnected = it
                )
            }
        }
    }

    private fun sendNavigateEvent(event: NavigationEvent){
        viewModelScope.launch {
            NavigationChannel.sendEvent(event)
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Refresh -> {
                loadData()
            }
            is HomeAction.ItemClickAction -> sendNavigateEvent(NavigationEvent.OnItemClick(action.movieId))
            is HomeAction.OnCategoryClick -> sendNavigateEvent(NavigationEvent.OnCategoryClick(action.movieCategory))
        }
    }
}