package com.example.movieapp.presentation.personalListFeature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.presentation.searchFeature.SearchAction
import com.example.movieapp.presentation.searchFeature.SearchBar
import com.example.movieapp.presentation.searchFeature.SearchState
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.MovieListCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun PersonalListRoot(
    viewModel: PersonalListViewModel = koinViewModel(),

) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PersonalListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PersonalListScreen(
    state: PersonalListState,
    onAction: (PersonalListAction) -> Unit,
) {
    state.movieCollection[MovieCategory.FAVORITE]?.let {
        MovieList(
            movieItems = it,
            onAction = onAction
        )
    }
}

@Composable
private fun MovieList(
    modifier: Modifier = Modifier,
    movieItems: List<Movie>,
    onAction: (PersonalListAction) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(
            items = movieItems,
            key = { movie ->
                movie.kinopoiskId!!
            }
        ) { movie ->
            MovieListCard(
                movieItem = movie,
                modifier = Modifier.fillMaxWidth(),
                onItemClick = {
                    onAction(PersonalListAction.ItemClickAction(movie.kinopoiskId!!))
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MovieAppTheme {
        PersonalListScreen(
            state = PersonalListState(),
            onAction = {}
        )
    }
}