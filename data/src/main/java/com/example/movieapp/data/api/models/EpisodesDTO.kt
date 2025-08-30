package com.example.movieapp.data.api.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class EpisodesDTO(

    @SerialName("seasonNumber") var seasonNumber: Int? = null,
    @SerialName("episodeNumber") var episodeNumber: Int? = null,
    @SerialName("nameRu") var nameRu: String? = null,
    @SerialName("nameEn") var nameEn: String? = null,
    @SerialName("synopsis") var synopsis: String? = null,
    @SerialName("releaseDate") var releaseDate: String? = null

)