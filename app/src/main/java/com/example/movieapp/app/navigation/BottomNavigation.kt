package com.example.movieapp.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

object BottomNavigationListItem {
    val bottomItems = listOf(
        BottomNavigationItem(
            title = "Главное",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Screens.HomeScreen::class.qualifiedName!!
        ),
        BottomNavigationItem(
            title = "Поиск",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = Screens.SearchScreen::class.qualifiedName!!
        ),
        BottomNavigationItem(
            title = "Любимые",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            route = Screens.FavScreen::class.qualifiedName!!
        ),
    )
}