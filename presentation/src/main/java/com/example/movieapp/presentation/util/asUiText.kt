package com.example.movieapp.presentation.util

import com.example.movieapp.R
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Type
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result

fun Order.asUiText(): UiText {
    return when (this) {
        Order.RATING -> UiText.StringResource(
            R.string.order_raiting
        )

        Order.NUM_VOTE -> UiText.StringResource(
            R.string.order_num_voute
        )

        Order.YEAR -> UiText.StringResource(
            R.string.order_year
        )
    }
}

fun Type.asUiText(): UiText {
    return when (this) {
        Type.FILM -> UiText.StringResource(
            R.string.type_film
        )

        Type.VIDEO -> UiText.StringResource(
            R.string.type_video
        )

        Type.TV_SERIES -> UiText.StringResource(
            R.string.type_tv_series
        )

        Type.MINI_SERIES -> UiText.StringResource(
            R.string.type_mini_series
        )

        Type.TV_SHOW -> UiText.StringResource(
            R.string.type_tv_show
        )
    }
}

fun MovieCategory.asUiText(): UiText {
    return when (this) {
        MovieCategory.TOP_POPULAR_ALL -> UiText.StringResource(
            R.string.top_popular_all
        )
        MovieCategory.TOP_250_MOVIES -> UiText.StringResource(
            R.string.top_250_movies
        )
        MovieCategory.TOP_250_TV_SHOWS -> UiText.StringResource(
            R.string.top_250_tv_shows
        )
        MovieCategory.COMICS_THEME -> UiText.StringResource(
            R.string.comics_theme
        )
        MovieCategory.SEARCH_HISTORY -> UiText.StringResource(
            R.string.search_history
        )
        MovieCategory.FAVORITE -> UiText.StringResource(
            R.string.favorite
        )
    }
}

fun DataError.asUiText(): UiText{
    return when (this) {
        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet_error
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )
        else ->{}
    } as UiText
}

fun Result.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}
