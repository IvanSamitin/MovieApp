package com.example.movieapp.presentation.util

import com.example.movieapp.domain.model.MovieCategory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface NavigationEvent {
    data class OnItemClick(val id: Int) : NavigationEvent

    data object OnNavigateBack : NavigationEvent

    data class OnSeasonOverviewClick(val id: Int) : NavigationEvent
    data class OnCategoryClick(val category: MovieCategory) : NavigationEvent

}

object NavigationChannel {
    private val navigationChannel = Channel<NavigationEvent>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    suspend fun sendEvent(event: NavigationEvent) {
        navigationChannel.send(event)
    }
}