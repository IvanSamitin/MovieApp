package com.example.movieapp.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movieapp.app.navigation.Screens.*
import com.example.movieapp.app.util.ObserveAsEvents
import com.example.movieapp.presentation.featureHome.HomeRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsViewModel
import com.example.movieapp.presentation.featureMovieList.MovieListRoot
import com.example.movieapp.presentation.featureMovieList.MovieListViewModel
import com.example.movieapp.presentation.personalListFeature.PersonalListRoot
import com.example.movieapp.presentation.searchFeature.SearchRoot
import com.example.movieapp.presentation.seasonOverviewFeature.SeasonOverviewRoot
import com.example.movieapp.presentation.seasonOverviewFeature.SeasonOverviewScreen
import com.example.movieapp.presentation.seasonOverviewFeature.SeasonOverviewViewModel
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
        startDestination = "Home",
        modifier = Modifier
            .padding(padding)

    ) {
        composable("Home") {
            HomeNavHost()
        }
        composable("Search") {
            SearchNavHost()
        }
        composable("Favorite") {
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
                MovieDetailsScreen(
                    movieId = event.id
                )
            )

            is NavigationEvent.OnCategoryClick -> homeNavController.navigate(
                MovieListScreen(
                    category = event.category
                )
            )

            is NavigationEvent.OnNavigateBack -> homeNavController.navigateUp()

            is NavigationEvent.OnSeasonOverviewClick -> homeNavController.navigate(
                Screens.SeasonOverviewScreen(movieId = event.id)
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

        composable<Screens.SeasonOverviewScreen> {
            val args = it.toRoute<Screens.SeasonOverviewScreen>()
            val viewModel: SeasonOverviewViewModel =
                koinViewModel<SeasonOverviewViewModel>(parameters = { parametersOf(args.movieId) })
            SeasonOverviewRoot(viewModel)
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

            is NavigationEvent.OnNavigateBack -> searchNavController.navigateUp()

            is NavigationEvent.OnSeasonOverviewClick -> searchNavController.navigate(
                Screens.SeasonOverviewScreen(movieId = event.id)
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
        composable<Screens.SeasonOverviewScreen> {
            val args = it.toRoute<Screens.SeasonOverviewScreen>()
            val viewModel: SeasonOverviewViewModel =
                koinViewModel<SeasonOverviewViewModel>(parameters = { parametersOf(args.movieId) })
            SeasonOverviewRoot(viewModel)
        }
    }
}

@Composable
fun FavNavHost() {
    val favNavController = rememberNavController()
    ObserveAsEvents(NavigationChannel.navigationEventsChannelFlow) { event ->
        when (event) {
            is NavigationEvent.OnItemClick -> favNavController.navigate(
                MovieDetailsScreen(
                    movieId = event.id
                )
            )

            is NavigationEvent.OnCategoryClick -> favNavController.navigate(
                MovieListScreen(
                    category = event.category
                )
            )

            is NavigationEvent.OnNavigateBack -> favNavController.navigateUp()

            is NavigationEvent.OnSeasonOverviewClick -> favNavController.navigate(
                Screens.SeasonOverviewScreen(movieId = event.id)
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
        composable<Screens.SeasonOverviewScreen> {
            val args = it.toRoute<Screens.SeasonOverviewScreen>()
            val viewModel: SeasonOverviewViewModel =
                koinViewModel<SeasonOverviewViewModel>(parameters = { parametersOf(args.movieId) })
            SeasonOverviewRoot(viewModel)
        }
    }
}

