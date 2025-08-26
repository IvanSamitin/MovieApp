package com.example.movieapp.presentation.searchFeature

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onAction = viewModel::onAction

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

        when {
            state.error.isNotBlank() -> {
                ErrorState(modifier = Modifier.padding(), onAction, state.error)
            }

            state.loading -> {
                LoadingIndicator()
            }
        }
        MovieList(movieItems = state.listMovie, onAction = onAction, state = state)
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    state: SearchState,
    onAction: (SearchAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = state.searchText,
        onValueChange = { onAction(SearchAction.OnSearchTextChange(it)) },
        placeholder = { Text(text = "Поиск") },
        trailingIcon = {
            IconButton(onClick = { onAction(SearchAction.OnSearchClick) }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        },
        supportingText = {
            AnimatedVisibility(visible = state.isInputTextError) {
                Text(text = state.searchErrorText)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onAction(SearchAction.OnSearchClick)
                focusManager.clearFocus()
            }
        ),
        isError = state.isInputTextError,
        singleLine = true,
        modifier = modifier

    )
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
            Button(onClick = { onAction(SearchAction.OnError) }) {
                Text(text = "Назад")
            }
        }
    }
}


@Composable
private fun MovieList(
    modifier: Modifier = Modifier,
    state: SearchState,
    movieItems: List<Movie>,
    onAction: (SearchAction) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        stickyHeader {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background),
                state = state,
                onAction = onAction
            )
        }
        items(
            items = movieItems,
            key = { movie ->
                movie.kinopoiskId!!
            }
        ) { movie ->
            MovieListCard(
                movieItem = movie,
                modifier = Modifier.fillMaxWidth(),
                onItemClick = { onAction(SearchAction.OnItemClick(it)) }
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