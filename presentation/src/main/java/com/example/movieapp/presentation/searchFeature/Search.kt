package com.example.movieapp.presentation.searchFeature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import com.example.movieapp.presentation.ui.uiComponents.MovieListCard
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchRoot(
    viewModel: SearchViewModel = koinViewModel(),
    onItemClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is SearchAction.onItemClick -> onItemClick(action.id)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(
            value = state.searchText,
            onValueChange = { onAction(SearchAction.onSearchTextChange(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Поиск") },
            trailingIcon = {
                IconButton(onClick = { onAction(SearchAction.onSearchClick) }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            },
            isError = state.inputTextError,
            singleLine = true
        )
        when {
            state.error.isNotBlank() -> {
                ErrorState(modifier = Modifier.padding(), onAction, state.error)
            }
            state.loading ->{
                LoadingIndicator()
            }
        }
        MovieList(movieItems = state.listMovie, onAction = onAction)
    }
}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    onAction: (SearchAction) -> Unit,
    errorText: String,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = errorText)
            Button(onClick = { onAction }) {
                Text(text = "Обновить")
            }
        }
    }
}


@Composable
private fun MovieList(
    modifier: Modifier = Modifier,
    movieItems: List<Movie>,
    onAction: (SearchAction) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(movieItems) { movie ->
            MovieListCard(
                movieItem = movie,
                modifier = Modifier.fillMaxWidth(),
                onItemClick = { onAction(SearchAction.onItemClick(it)) }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MovieAppTheme {
        SearchScreen(
            state = SearchState(),
            onAction = {}
        )
    }
}