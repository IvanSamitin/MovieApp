package com.example.movieapp.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDTO(
    @SerialName("kinopoiskId") var kinopoiskId: Int? = null,
    @SerialName("kinopoiskHDId") var kinopoiskHDId: String? = null,
    @SerialName("imdbId") var imdbId: String? = null,
    @SerialName("nameRu") var nameRu: String? = null,
    @SerialName("nameEn") var nameEn: String? = null,
    @SerialName("nameOriginal") var nameOriginal: String? = null,
    @SerialName("posterUrl") var posterUrl: String? = null,
    @SerialName("posterUrlPreview") var posterUrlPreview: String? = null,
    @SerialName("coverUrl") var coverUrl: String? = null,
    @SerialName("logoUrl") var logoUrl: String? = null,
    @SerialName("reviewsCount") var reviewsCount: Int? = null,
    @SerialName("ratingGoodReview") var ratingGoodReview: Double? = null,
    @SerialName("ratingGoodReviewVoteCount") var ratingGoodReviewVoteCount: Int? = null,
    @SerialName("ratingKinopoisk") var ratingKinopoisk: Double? = null,
    @SerialName("ratingKinopoiskVoteCount") var ratingKinopoiskVoteCount: Int? = null,
    @SerialName("ratingImdb") var ratingImdb: Double? = null,
    @SerialName("ratingImdbVoteCount") var ratingImdbVoteCount: Int? = null,
    @SerialName("ratingFilmCritics") var ratingFilmCritics: Double? = null,
    @SerialName("ratingFilmCriticsVoteCount") var ratingFilmCriticsVoteCount: Int? = null,
    @SerialName("ratingAwait") var ratingAwait: String? = null,
    @SerialName("ratingAwaitCount") var ratingAwaitCount: Int? = null,
    @SerialName("ratingRfCritics") var ratingRfCritics: Double? = null,
    @SerialName("ratingRfCriticsVoteCount") var ratingRfCriticsVoteCount: Int? = null,
    @SerialName("webUrl") var webUrl: String? = null,
    @SerialName("year") var year: Int? = null,
    @SerialName("filmLength") var filmLength: Int? = null,
    @SerialName("slogan") var slogan: String? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("shortDescription") var shortDescription: String? = null,
    @SerialName("editorAnnotation") var editorAnnotation: String? = null,
    @SerialName("isTicketsAvailable") var isTicketsAvailable: Boolean? = null,
    @SerialName("productionStatus") var productionStatus: ProductionStatusDTO? = null,
    @SerialName("type") var type: TypeDTO? = null,
    @SerialName("ratingMpaa") var ratingMpaa: String? = null,
    @SerialName("ratingAgeLimits") var ratingAgeLimits: String? = null,
    @SerialName("countries") var countries: List<CountriesDTO>? = null,
    @SerialName("genres") var genres: List<GenresDTO>? = null,
    @SerialName("startYear") var startYear: String? = null,
    @SerialName("endYear") var endYear: String? = null,
    @SerialName("serial") var serial: Boolean? = null,
    @SerialName("shortFilm") var shortFilm: Boolean? = null,
    @SerialName("completed") var completed: Boolean? = null,
    @SerialName("hasImax") var hasImax: Boolean? = null,
    @SerialName("has3D") var has3D: Boolean? = null,
    @SerialName("lastSync") var lastSync: String?  = null
)

@Serializable
data class CountriesDTO(
    @SerialName("country") var country: String? = null
)

@Serializable
data class GenresDTO(
    @SerialName("genre") var genre: String? = null
)

@Serializable
enum class ProductionStatusDTO {
    FILMING,
    PRE_PRODUCTION,
    COMPLETED,
    ANNOUNCED,
    UNKNOWN,
    POST_PRODUCTION
}

@Serializable
enum class TypeDTO {
    FILM,
    VIDEO,
    TV_SERIES,
    MINI_SERIES,
    TV_SHOW
}
