package com.example.movieapp.presentation.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.presentation.featureHome.HomeRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsViewModel
import com.example.movieapp.presentation.featureMovieList.MovieListRoot
import com.example.movieapp.presentation.featureMovieList.MovieListViewModel
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.HomeScreen,
                ) {
                    composable<Screens.HomeScreen> {
                        HomeRoot(
                            onItemClick = {
                                navController.navigate(Screens.MovieDetailsScreen(movieId = it))
                            },
                            onCategoryClick = {
                                navController.navigate(Screens.MovieListScreen(category = it))
                            }
                        )


                    }
                    composable<Screens.MovieListScreen> {
                        val args = it.toRoute<Screens.MovieListScreen>()
                        val viewModel: MovieListViewModel =
                            koinViewModel<MovieListViewModel>(parameters = { parametersOf(args.category) })
                        MovieListRoot(
                            onItemClick = { movieId ->
                                navController.navigate(Screens.MovieDetailsScreen(movieId = movieId))
                            },
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
        }
    }
}

sealed interface Screens {
    @Serializable
    data object HomeScreen : Screens

    @Serializable
    data class MovieListScreen(
        val category: MovieCategory
    ) : Screens

    @Serializable
    data class MovieDetailsScreen(
        val movieId: Int,
    ) : Screens
}

