package com.example.movieapp.data.mappers

import com.example.movieapp.data.api.models.EpisodesDTO
import com.example.movieapp.data.api.models.ItemsDTO
import com.example.movieapp.domain.model.Episodes
import com.example.movieapp.domain.model.Season

internal fun ItemsDTO.toSeason(): Season =
    Season(
        number = number!!,
        episodes = episodes.map { it.toEpisodes() }
    )

internal fun EpisodesDTO.toEpisodes(): Episodes =
    Episodes(
        seasonNumber = seasonNumber!!,
        episodeNumber = episodeNumber!!,
        nameRu = nameRu,
        nameEn = nameEn,
        synopsis = synopsis,
        releaseDate = releaseDate
    )
