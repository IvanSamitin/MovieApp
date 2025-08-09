package com.example.movieapp.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilmCollectionResponseDTO(
    @SerialName("total") var total: Int?,
    @SerialName("totalPages") var totalPages: Int?,
    @SerialName("items") var items: List<MovieDTO>
)