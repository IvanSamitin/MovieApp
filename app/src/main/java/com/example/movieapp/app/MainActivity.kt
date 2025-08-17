package com.example.movieapp.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.presentation.featureHome.HomeRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsRoot
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsViewModel
import com.example.movieapp.presentation.featureMovieList.MovieListRoot
import com.example.movieapp.presentation.featureMovieList.MovieListViewModel
import com.example.movieapp.presentation.searchFeature.SearchRoot
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
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                Scaffold(bottomBar = {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        BottomNavigationListItem.bottomItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route.route(),
                                onClick = {
                                    selectedItemIndex = index
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        restoreState = true
                                    }
                                },
                                label = {
                                    Text(text = item.title)
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            )
                        }
                    }
                }) { innerPadding ->
                    NavHostContainer(navController = navController, padding = innerPadding)
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Screens
)

object BottomNavigationListItem {
    val bottomItems = listOf(
        BottomNavigationItem(
            title = "Главное",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Screens.HomeScreen
        ),
        BottomNavigationItem(
            title = "Поиск",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = Screens.SearchScreen
        ),
//        BottomNavigationItem(
//            title = "Настройки",
//            selectedIcon = Icons.Filled.Settings,
//            unselectedIcon = Icons.Outlined.Settings,
//            route = Screens.HomeScreen
//        ),
    )
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen,
        modifier = Modifier.padding(padding)
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
        composable<Screens.SearchScreen> {
            SearchRoot(
                onItemClick = { movieId ->
                    navController.navigate(Screens.MovieDetailsScreen(movieId = movieId))
                },
            )
        }
    }

}

@Serializable
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

    @Serializable
    data object SearchScreen : Screens
}

fun Screens.route(): String {
    return when (this) {
        is Screens.HomeScreen -> "com.example.movieapp.app.Screens.HomeScreen"
        is Screens.SearchScreen -> "com.example.movieapp.app.Screens.SearchScreen"
        else -> ""
    }
}
