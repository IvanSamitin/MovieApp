package com.example.movieapp.data.mappers

import com.example.movieapp.data.api.models.CountriesDTO
import com.example.movieapp.data.api.models.GenresDTO
import com.example.movieapp.data.api.models.MovieDTO
import com.example.movieapp.data.api.models.ProductionStatusDTO
import com.example.movieapp.data.api.models.TypeDTO
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.local.entity.ProductionStatusEntity
import com.example.movieapp.data.local.entity.TypeEntity
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.ProductionStatus
import com.example.movieapp.domain.model.Type

internal fun MovieDTO.toMovie(): Movie =
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

internal fun MovieEntity.toMovieDto(): MovieDTO =
    MovieDTO(
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
                ProductionStatusEntity.FILMING -> ProductionStatusDTO.FILMING
                ProductionStatusEntity.PRE_PRODUCTION -> ProductionStatusDTO.PRE_PRODUCTION
                ProductionStatusEntity.COMPLETED -> ProductionStatusDTO.COMPLETED
                ProductionStatusEntity.ANNOUNCED -> ProductionStatusDTO.ANNOUNCED
                ProductionStatusEntity.UNKNOWN -> ProductionStatusDTO.UNKNOWN
                ProductionStatusEntity.POST_PRODUCTION -> ProductionStatusDTO.POST_PRODUCTION
                null -> ProductionStatusDTO.UNKNOWN
            },
        type =
            when (type) {
                TypeEntity.FILM -> TypeDTO.FILM
                TypeEntity.TV_SERIES -> TypeDTO.TV_SERIES
                TypeEntity.MINI_SERIES -> TypeDTO.MINI_SERIES
                TypeEntity.TV_SHOW -> TypeDTO.TV_SHOW
                TypeEntity.VIDEO -> TypeDTO.VIDEO
                else -> TypeDTO.FILM
            },

        ratingMpaa = ratingMpaa,
        ratingAgeLimits = ratingAgeLimits,
        countries = countries?.map { CountriesDTO(it) },
        genres = genres?.map{ GenresDTO(it)},
        startYear = startYear,
        endYear = endYear,
        serial = serial,
        shortFilm = shortFilm,
        completed = completed,
    )


internal fun MovieEntity.toMovie(): Movie =
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
                ProductionStatusEntity.FILMING -> ProductionStatus.FILMING
                ProductionStatusEntity.PRE_PRODUCTION -> ProductionStatus.PRE_PRODUCTION
                ProductionStatusEntity.COMPLETED -> ProductionStatus.COMPLETED
                ProductionStatusEntity.ANNOUNCED -> ProductionStatus.ANNOUNCED
                ProductionStatusEntity.UNKNOWN -> ProductionStatus.UNKNOWN
                ProductionStatusEntity.POST_PRODUCTION -> ProductionStatus.POST_PRODUCTION
                null -> ProductionStatus.UNKNOWN
            },
        type =
            when (type) {
                TypeEntity.FILM -> Type.FILM
                TypeEntity.TV_SERIES -> Type.TV_SERIES
                TypeEntity.MINI_SERIES -> Type.MINI_SERIES
                TypeEntity.TV_SHOW -> Type.TV_SHOW
                TypeEntity.VIDEO -> Type.VIDEO
                else -> Type.FILM
            },

        ratingMpaa = ratingMpaa,
        ratingAgeLimits = ratingAgeLimits,
        countries = countries,
        genres = genres,
        startYear = startYear,
        endYear = endYear,
        serial = serial,
        shortFilm = shortFilm,
        completed = completed,
    )
