package com.example.movieapp.presentation.searchFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SearchState()
        )

    private fun searchMovie() {
        viewModelScope.launch {
            if (_state.value.searchText.trim().isBlank()) {
                _state.value = _state.value.copy(
                    inputTextError = true
                )
                return@launch
            }
            _state.value = _state.value.copy(
                loading = true,
                inputTextError = false
            )
            val result = movieRepository.searchMovie(keyword = _state.value.searchText.trim())
            when (result) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        listMovie = result.data.filter { it.nameRu != null },
                        loading = false,
                        error = if (result.data.isEmpty()) "По запросу ничего не найдено" else ""
                    )
                }

                is Result.Error -> {
                    _state.value = _state.value.copy(
                        error = result.error.toString()
                    )
                }
            }

        }
    }


    private fun onSearchTextChange(text: String) {
        _state.value = _state.value.copy(
            searchText = text
        )
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.onSearchTextChange -> onSearchTextChange(action.text)
            is SearchAction.onSearchClick -> searchMovie()
            else -> Unit
        }
    }
}