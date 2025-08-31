package com.example.movieapp.app.di

import com.example.movieapp.data.sync.MovieSyncWorker
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.presentation.featureHome.HomeViewModel
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsViewModel
import com.example.movieapp.presentation.featureMovieList.MovieListViewModel
import com.example.movieapp.presentation.personalListFeature.PersonalListViewModel
import com.example.movieapp.presentation.searchFeature.SearchViewModel
import com.example.movieapp.presentation.seasonOverviewFeature.SeasonOverviewViewModel
import com.example.movieapp.presentation.util.AndroidConnectivityObserver
import com.example.movieapp.presentation.util.ConnectivityObserver
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val mainModule =
    module {
        viewModel {
            HomeViewModel(get(), get())
        }

        single<ConnectivityObserver>{
            AndroidConnectivityObserver(get())
        }

        viewModel { (movieCategory: MovieCategory) ->
            MovieListViewModel(movieCategory = movieCategory, movieRepository = get())
        }

        viewModel { (movieId: Int) ->
            MovieDetailsViewModel(movieId = movieId, movieRepository = get())
        }
        viewModel {
            SearchViewModel(get())
        }
        viewModel {
            PersonalListViewModel(get())
        }

        viewModel { (movieId: Int) ->
            SeasonOverviewViewModel(movieRepository = get(), movieId = movieId)
        }

        worker { MovieSyncWorker(get(), get(), get()) }
    }


