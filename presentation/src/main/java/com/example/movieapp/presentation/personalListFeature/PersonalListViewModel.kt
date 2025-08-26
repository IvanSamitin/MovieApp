package com.example.movieapp.presentation.personalListFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import com.example.movieapp.presentation.util.NavigationChannel
import com.example.movieapp.presentation.util.NavigationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PersonalListViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(PersonalListState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                getCollection(MovieCategory.FAVORITE)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PersonalListState()
        )


    private fun getCollection(movieCategory: MovieCategory) {
        viewModelScope.launch {
            movieRepository.getCollection(type = movieCategory, 1).collect { result ->
                when (result) {
                    is Result.Error -> {
                        val errorMessage =
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> "Отсутсвует интернет"
                                DataError.Network.SERVER_ERROR -> "Ошибка сервера"
                            }
                    }

                    is Result.Success -> {
                        _state.value =
                            _state.value.copy(
                                movieCollection = _state.value.movieCollection + (movieCategory to result.data),
                            )
                    }
                }
            }
        }
    }

    private fun navigateToMovieDetails(id: Int){
        viewModelScope.launch {
            NavigationChannel.sendEvent(NavigationEvent.OnItemClick(id))
        }
    }

    fun onAction(action: PersonalListAction) {
        when (action) {
            is PersonalListAction.ItemClickAction -> navigateToMovieDetails(action.movieId)
        }
    }

}