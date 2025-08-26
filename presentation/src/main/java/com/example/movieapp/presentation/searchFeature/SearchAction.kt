package com.example.movieapp.presentation.searchFeature

sealed interface SearchAction {
    data class OnSearchTextChange(val text: String): SearchAction
    data object OnSearchClick: SearchAction
    data class OnItemClick(val id: Int): SearchAction

    data object OnError: SearchAction
}