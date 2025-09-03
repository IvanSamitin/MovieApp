package com.example.movieapp.presentation.featureMovieList

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.presentation.util.asUiText


class MovieListViewModel(
    movieRepository: MovieRepository,
    movieCategory: MovieCategory,
) : ViewModel() {

    var categoryState = mutableStateOf(movieCategory.asUiText())
        private set
    val pagingData = movieRepository
        .getPagingCollection(movieCategory)
        .cachedIn(viewModelScope)
}
