package com.example.movieapp.data.mappers

import com.example.movieapp.data.api.models.MovieDTO
import com.example.movieapp.data.api.models.ProductionStatusDTO
import com.example.movieapp.data.api.models.TypeDTO
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.ProductionStatus
import com.example.movieapp.domain.model.Type

fun MovieDTO.toMovie(): Movie =
    Movie(
        kinopoiskId = kinopoiskId,
        nameRu = nameRu,
        nameOriginal = nameOriginal,
        posterUrl = posterUrl,
        posterUrlPreview = posterUrlPreview,
        coverUrl = coverUrl,
        logoUrl = logoUrl,
        reviewsCount = reviewsCount,
        ratingGoodReview = ratingGoodReview,
        ratingGoodReviewVoteCount = ratingGoodReviewVoteCount,
        ratingKinopoisk = ratingKinopoisk,
        ratingKinopoiskVoteCount = ratingKinopoiskVoteCount,
        ratingImdb = ratingImdb,
        ratingImdbVoteCount = ratingImdbVoteCount,
        ratingFilmCritics = ratingFilmCritics,
        ratingFilmCriticsVoteCount = ratingFilmCriticsVoteCount,
        ratingAwait = ratingAwait,
        ratingAwaitCount = ratingAwaitCount,
        ratingRfCritics = ratingRfCritics,
        ratingRfCriticsVoteCount = ratingRfCriticsVoteCount,
        year = year,
        filmLength = filmLength,
        slogan = slogan,
        description = description,
        shortDescription = shortDescription,
        editorAnnotation = editorAnnotation,
        isTicketsAvailable = isTicketsAvailable,
        productionStatus =
            when (productionStatus) {
                ProductionStatusDTO.FILMING -> ProductionStatus.FILMING
                ProductionStatusDTO.PRE_PRODUCTION -> ProductionStatus.PRE_PRODUCTION
                ProductionStatusDTO.COMPLETED -> ProductionStatus.COMPLETED
                ProductionStatusDTO.ANNOUNCED -> ProductionStatus.ANNOUNCED
                ProductionStatusDTO.UNKNOWN -> ProductionStatus.UNKNOWN
                ProductionStatusDTO.POST_PRODUCTION -> ProductionStatus.POST_PRODUCTION
                null -> ProductionStatus.UNKNOWN
            },
        type =
            when (type) {
                TypeDTO.FILM -> Type.FILM
                TypeDTO.TV_SERIES -> Type.TV_SERIES
                TypeDTO.MINI_SERIES -> Type.MINI_SERIES
                TypeDTO.TV_SHOW -> Type.TV_SHOW
                TypeDTO.VIDEO -> Type.VIDEO
                else -> Type.FILM
            },
        ratingMpaa = ratingMpaa,
        ratingAgeLimits = ratingAgeLimits,
        countries = countries?.mapNotNull { it.country } ?: emptyList(),
        genres = genres?.mapNotNull { it.genre } ?: emptyList(),
        startYear = startYear,
        endYear = endYear,
        serial = serial,
        shortFilm = shortFilm,
        completed = completed,
    )
