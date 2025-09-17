package com.example.movieapp.domain.model

data class Movie(
    val kinopoiskId: Int?,
    val nameRu: String?,
    val nameOriginal: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val coverUrl: String?,
    val logoUrl: String?,
    val reviewsCount: Int?,
    val ratingGoodReview: Double?,
    val ratingGoodReviewVoteCount: Int?,
    val ratingKinopoisk: Double?,
    val ratingKinopoiskVoteCount: Int?,
    val ratingImdb: Double?,
    val ratingImdbVoteCount: Int?,
    val ratingFilmCritics: Double?,
    val ratingFilmCriticsVoteCount: Int?,
    val ratingAwait: String?,
    val ratingAwaitCount: Int?,
    val ratingRfCritics: Double?,
    val ratingRfCriticsVoteCount: Int?,
    val webUrl: String?,
    val year: Int?,
    val filmLength: Int? ,
    val slogan: String?,
    val description: String?,
    val shortDescription: String?,
    val editorAnnotation: String?,
    val isTicketsAvailable: Boolean?,
    val productionStatus: ProductionStatus?,
    val type: Type?,
    val ratingMpaa: String?,
    val ratingAgeLimits: String?,
    val countries: List<String>?,
    val genres: List<String>?,
    val startYear: Int?,
    val endYear: Int?,
    val serial: Boolean?,
    val shortFilm: Boolean?,
    val completed: Boolean?
)

enum class ProductionStatus {
    FILMING,
    PRE_PRODUCTION,
    COMPLETED,
    ANNOUNCED,
    UNKNOWN,
    POST_PRODUCTION,
}

enum class Type{
    FILM,
    VIDEO,
    TV_SERIES,
    MINI_SERIES,
    TV_SHOW;

}
