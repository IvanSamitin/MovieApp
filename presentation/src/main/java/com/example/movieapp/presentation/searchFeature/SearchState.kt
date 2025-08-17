package com.example.movieapp.presentation.searchFeature

import com.example.movieapp.domain.model.Movie

data class SearchState(
    val searchText: String = "",
    val listMovie: List<Movie> = emptyList(),
    val isSearching: Boolean = false,
    val loading: Boolean = false,
    val error: String = "",
    val inputTextError: Boolean = false
)