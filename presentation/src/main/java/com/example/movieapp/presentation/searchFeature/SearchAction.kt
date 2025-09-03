package com.example.movieapp.presentation.searchFeature

import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Type

sealed interface SearchAction {
    data class OnSearchTextChange(val text: String) : SearchAction
    data object OnSearchClick : SearchAction
    data class OnItemClick(val id: Int) : SearchAction

    data object ShowFilters : SearchAction
    data object OnError : SearchAction

    data class OnSliderChange(val sliderState: ClosedFloatingPointRange<Float>) : SearchAction

    data class OnTypeChange(val typeMenuValue: Type) : SearchAction

    data class OnOrderChange(val orderMenuValue: Order) : SearchAction

    data object BackHandler : SearchAction

    data object OnScrollCompleted : SearchAction
}