package com.example.movieapp.presentation.personalListFeature

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory

data class PersonalListState(
    val movieCollection: Map<MovieCategory, List<Movie>> = emptyMap(),

)