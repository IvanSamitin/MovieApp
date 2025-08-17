package com.example.movieapp.presentation.searchFeature

sealed interface SearchAction {
    data class onSearchTextChange(val text: String): SearchAction
    data object onSearchClick: SearchAction
    data class onItemClick(val id: Int): SearchAction
}