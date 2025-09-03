package com.example.movieapp.presentation.personalListFeature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
import com.example.movieapp.presentation.ui.uiComponents.MovieList
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieList(
    modifier: Modifier = Modifier,
    movieItems: List<Movie>,
    onAction: (PersonalListAction) -> Unit,
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(text = "Любимое")
            },
            windowInsets = WindowInsets(top = 0.dp)
        )
        if (movieItems.isEmpty()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                Text(
                    text = "Добавьте ваши любимые фильмы",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        } else {
            MovieList(
                movieItems = movieItems,
            ) { movie ->
                onAction(PersonalListAction.ItemClickAction(movie))
            }
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