package com.example.movieapp.presentation.ui.uiComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieapp.domain.model.Movie

@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    movieItems: List<Movie>,
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (movieId: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(4.dp)
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
                onItemClick = onItemClick
            )
        }
    }
}