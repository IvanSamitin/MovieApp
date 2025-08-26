package com.example.movieapp.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.movieapp.app.navigation.Screens
import com.example.movieapp.app.util.ObserveAsEvents
import com.example.movieapp.presentation.featureHome.HomeRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsViewModel
import com.example.movieapp.presentation.featureMovieList.MovieListRoot
import com.example.movieapp.presentation.featureMovieList.MovieListViewModel
import com.example.movieapp.presentation.personalListFeature.PersonalListRoot
import com.example.movieapp.presentation.searchFeature.SearchRoot
import com.example.movieapp.presentation.util.NavigationChannel
import com.example.movieapp.presentation.util.NavigationEvent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {
    ObserveAsEvents(NavigationChannel.navigationEventsChannelFlow) { event ->
        when (event) {
            is NavigationEvent.OnItemClick -> navController.navigate(
                Screens.MovieDetailsScreen(
                    movieId = event.id
                )
            )
            is NavigationEvent.OnCategoryClick -> navController.navigate(
                Screens.MovieListScreen(
                    category = event.category
                )
            )
        }
    }
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen,
        modifier = Modifier
            .padding(padding)
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))

    ) {
        composable<Screens.HomeScreen> {
            HomeRoot()
        }
        composable<Screens.MovieListScreen> {
            val args = it.toRoute<Screens.MovieListScreen>()
            val viewModel: MovieListViewModel =
                koinViewModel<MovieListViewModel>(parameters = { parametersOf(args.category) })
            MovieListRoot(
                viewModel = viewModel
            )
        }
        composable<Screens.MovieDetailsScreen> {
            val args = it.toRoute<Screens.MovieDetailsScreen>()
            val viewModel: MovieDetailsViewModel =
                koinViewModel<MovieDetailsViewModel>(parameters = { parametersOf(args.movieId) })
            MovieDetailsRoot(viewModel)
        }
        composable<Screens.SearchScreen>{
            SearchRoot()
        }

        composable<Screens.FavScreen> {
            PersonalListRoot()
        }
    }
}
