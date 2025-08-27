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
import androidx.navigation.compose.rememberNavController
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

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen,
        modifier = Modifier
            .padding(padding)
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))

    ) {
        composable<Screens.HomeScreen> {
            HomeNavHost()
        }
        composable<Screens.SearchScreen> {
            SearchNavHost()
        }
        composable<Screens.FavScreen> {
            FavNavHost()
        }
    }
}

@Composable
fun HomeNavHost() {
    val homeNavController = rememberNavController()
    ObserveAsEvents(NavigationChannel.navigationEventsChannelFlow) { event ->
        when (event) {
            is NavigationEvent.OnItemClick -> homeNavController.navigate(
                Screens.MovieDetailsScreen(
                    movieId = event.id
                )
            )

            is NavigationEvent.OnCategoryClick -> homeNavController.navigate(
                Screens.MovieListScreen(
                    category = event.category
                )
            )
        }
    }
    NavHost(homeNavController, startDestination = Screens.HomeScreen) {
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
    }
}

@Composable
fun SearchNavHost() {
    val searchNavController = rememberNavController()
    ObserveAsEvents(NavigationChannel.navigationEventsChannelFlow) { event ->
        when (event) {
            is NavigationEvent.OnItemClick -> searchNavController.navigate(
                Screens.MovieDetailsScreen(
                    movieId = event.id
                )
            )

            is NavigationEvent.OnCategoryClick -> searchNavController.navigate(
                Screens.MovieListScreen(
                    category = event.category
                )
            )
        }
    }
    NavHost(searchNavController, startDestination = Screens.SearchScreen) {
        composable<Screens.SearchScreen> {
            SearchRoot()
        }
        composable<Screens.MovieDetailsScreen> {
            val args = it.toRoute<Screens.MovieDetailsScreen>()
            val viewModel: MovieDetailsViewModel =
                koinViewModel<MovieDetailsViewModel>(parameters = { parametersOf(args.movieId) })
            MovieDetailsRoot(viewModel)
        }
    }
}

@Composable
fun FavNavHost() {
    val favNavController = rememberNavController()
    ObserveAsEvents(NavigationChannel.navigationEventsChannelFlow) { event ->
        when (event) {
            is NavigationEvent.OnItemClick -> favNavController.navigate(
                Screens.MovieDetailsScreen(
                    movieId = event.id
                )
            )

            is NavigationEvent.OnCategoryClick -> favNavController.navigate(
                Screens.MovieListScreen(
                    category = event.category
                )
            )
        }
    }
    NavHost(favNavController, startDestination = Screens.FavScreen) {
        composable<Screens.FavScreen> {
            PersonalListRoot()
        }
        composable<Screens.MovieDetailsScreen> {
            val args = it.toRoute<Screens.MovieDetailsScreen>()
            val viewModel: MovieDetailsViewModel =
                koinViewModel<MovieDetailsViewModel>(parameters = { parametersOf(args.movieId) })
            MovieDetailsRoot(viewModel)
        }
    }
}

