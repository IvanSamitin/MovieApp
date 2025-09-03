package com.example.movieapp.presentation.searchFeature

import androidx.compose.foundation.lazy.LazyListState
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Type
import com.example.movieapp.presentation.util.UiText

data class SearchState(
    val searchText: String = "",
    val listMovie: List<Movie> = emptyList(),
    val loading: Boolean = false,
    val error: UiText? = null,
    val isInputTextError: Boolean = false,
    val searchErrorText: String = "",
    val isDialogOpen: Boolean = false,
    val sliderState: ClosedFloatingPointRange<Float> = 0f..10f,
    val typeMenuValue: Type? = null,
    val orderMenuValue: Order? = null,
    val isSearched: Boolean = false,

    val scrollToPosition: Int? = null
)

