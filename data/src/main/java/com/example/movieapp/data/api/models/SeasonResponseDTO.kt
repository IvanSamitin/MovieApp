package com.example.movieapp.data.api.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class SeasonResponseDTO(

    @SerialName("total") var total: Int? = null,
    @SerialName("items") var items: List<ItemsDTO> = emptyList()

)