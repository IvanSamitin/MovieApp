package com.example.movieapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsViewModel
import com.example.movieapp.presentation.featureMovieList.MovieListRoot
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
                    startDestination = MovieListScreenObject
                ) {
                    composable<MovieListScreenObject> {
                        MovieListRoot(onItemClick = {
                            navController.navigate(MovieDetailsScreenObject(movieId = it))
                        })
                    }
                    composable<MovieDetailsScreenObject> {
                        val args = it.toRoute<MovieDetailsScreenObject>()
                        val viewModel: MovieDetailsViewModel =
                            koinViewModel<MovieDetailsViewModel>(parameters = { parametersOf(args.movieId) })
                        MovieDetailsRoot(viewModel)
                    }
                }

            }
        }
    }
}


@Serializable
object MovieListScreenObject

@Serializable
data class MovieDetailsScreenObject(val movieId: Int)
