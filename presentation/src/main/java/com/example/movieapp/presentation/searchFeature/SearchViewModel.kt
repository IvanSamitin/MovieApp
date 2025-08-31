package com.example.movieapp.presentation.searchFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Type
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import com.example.movieapp.presentation.util.NavigationChannel
import com.example.movieapp.presentation.util.NavigationEvent
import com.example.movieapp.presentation.util.NavigationEvent.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.String

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
                    isInputTextError = true,
                    searchErrorText = "Поиск не может быт пустым"
                )
                return@launch
            }
            _state.value = _state.value.copy(
                loading = true,
                isInputTextError = false,
                searchErrorText = ""
            )
            val result = movieRepository.searchMovie(
                keyword = _state.value.searchText.trim(),
                ratingFrom = state.value.sliderState.start,
                ratingTo = state.value.sliderState.endInclusive,
                order = if (state.value.orderMenuValue.isNotBlank()) {
                    Order.valueOf(state.value.orderMenuValue)
                } else null,
                type = if (state.value.typeMenuValue.isNotBlank()) Type.valueOf(state.value.typeMenuValue) else null
            )
            when (result) {
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        listMovie = result.data.filter { it.nameRu != null },
                        loading = false,
                        error = if (result.data.isEmpty()) "По запросу ничего не найдено" else ""
                    )
                }

                is Result.Error -> {
                    val errorMessage =
                        when (result.error) {
                            DataError.Network.NO_INTERNET -> "Отсутсвует интернет"
                            DataError.Network.SERVER_ERROR -> "Ошибка сервера"
                        }
                    _state.value = _state.value.copy(
                        error = errorMessage
                    )
                }
            }
        }
    }

    private fun sendNavigateEvent(event: NavigationEvent) {
        viewModelScope.launch {
            NavigationChannel.sendEvent(event)
        }
    }

    private fun onSearchTextChange(text: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                searchText = text
            )
        }
    }

    private fun onErrorAction() {
        _state.value = _state.value.copy(
            searchText = "",
            listMovie = emptyList(),
            loading = false,
            error = "",
            isInputTextError = false,
            searchErrorText = ""
        )
    }

    private fun updateDialog() {
        _state.value = state.value.copy(
            isDialogOpen = !state.value.isDialogOpen
        )
    }

    private fun updateSlider(sliderState: ClosedFloatingPointRange<Float>) {
        _state.value = state.value.copy(
            sliderState = sliderState
        )
    }

    private fun updateType(type: String) {
        _state.value = state.value.copy(
            typeMenuValue = type
        )
    }

    private fun updateOrder(order: String) {
        _state.value = state.value.copy(
            orderMenuValue = order
        )
    }


    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnSearchTextChange -> onSearchTextChange(action.text)
            is SearchAction.OnSearchClick -> searchMovie()
            is SearchAction.OnItemClick -> sendNavigateEvent(OnItemClick(action.id))
            is SearchAction.OnError -> onErrorAction()
            is SearchAction.ShowFilters -> updateDialog()
            is SearchAction.OnSliderChange -> updateSlider(action.sliderState)
            is SearchAction.OnOrderChange -> updateOrder(action.orderMenuValue)
            is SearchAction.OnTypeChange -> updateType(action.typeMenuValue)
        }
    }
}

