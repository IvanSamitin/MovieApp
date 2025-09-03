package com.example.movieapp.presentation.personalListFeature

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.presentation.util.UiText

data class PersonalListState(
    val movieCollection: Map<MovieCategory, List<Movie>> = emptyMap(),
    val errorText: UiText? = null

)