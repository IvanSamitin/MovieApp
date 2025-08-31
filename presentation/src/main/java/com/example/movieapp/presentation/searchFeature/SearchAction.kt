package com.example.movieapp.presentation.searchFeature

sealed interface SearchAction {
    data class OnSearchTextChange(val text: String): SearchAction
    data object OnSearchClick: SearchAction
    data class OnItemClick(val id: Int): SearchAction

    data object ShowFilters : SearchAction
    data object OnError: SearchAction

    data class OnSliderChange(val sliderState: ClosedFloatingPointRange<Float>) : SearchAction

    data class OnTypeChange(val typeMenuValue: String) : SearchAction

    data class OnOrderChange(val orderMenuValue: String) : SearchAction
}