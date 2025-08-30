package com.example.movieapp.data.api.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class ItemsDTO(

    @SerialName("number") var number: Int? = null,
    @SerialName("episodes") var episodes: List<EpisodesDTO> = emptyList()

)