package com.example.movieapp.presentation.personalListFeature

import com.example.movieapp.presentation.featureHome.HomeAction

sealed interface PersonalListAction {
    data class ItemClickAction(val movieId: Int): PersonalListAction
}