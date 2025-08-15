package com.example.movieapp.presentation.featureMovieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository


class MovieListViewModel(
    movieRepository: MovieRepository,
    movieCategory: MovieCategory,
) : ViewModel() {

    val pagingData = movieRepository
        .getPagingCollection(movieCategory)
        .cachedIn(viewModelScope)
}
