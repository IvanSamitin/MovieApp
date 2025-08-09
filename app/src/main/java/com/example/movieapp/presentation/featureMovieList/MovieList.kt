package com.example.movieapp.presentation.featureMovieList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.uiComponents.PullToRefreshLazyColumn
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieListRoot(
    viewModel: MovieListViewModel = koinViewModel(),
    onItemClick: (movieId: Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieListScreen(
        state = state,
        onAction = { action ->
            when(action){
                is MovieListAction.ItemClickAction -> onItemClick(action.id)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun MovieListScreen(
    state: MovieListState,
    onAction: (MovieListAction) -> Unit,
) {
    Scaffold(
        topBar = { TopBar() }
    ) { padding ->
        when {
            state.loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(padding))
            }

            state.error != null -> {
                ErrorScreen(
                    modifier = Modifier.padding(padding),
                    onAction = { onAction(MovieListAction.ActionUpdate) },
                    error = state.error
                )
            }

            state.movieDetails != null -> {
                MovieList(
                    movies = state.movieDetails,
                    modifier = Modifier
                        .padding(padding),
                    onAction = onAction,
                    state = state
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(title = { Text(text = "Топ 250 лучших фильмов") }, modifier = modifier)
}

@Composable
private fun MovieList(
    modifier: Modifier = Modifier,
    movies: List<Movie>,
    onAction: (MovieListAction) -> Unit,
    state: MovieListState,

    ) {
    PullToRefreshLazyColumn(
        modifier = modifier,
        items = movies,
        content = { movie ->
            MovieListCard(
                movieItem = movie,
                modifier = Modifier.fillMaxWidth(),
                onAction = onAction
            )
        },
        isRefreshing = state.isRefreshing,
        onRefresh = { onAction(MovieListAction.ActionUpdate) }
    )

//    LazyColumn(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        contentPadding = PaddingValues(horizontal = 4.dp)
//    ) {
//        items(movies) { movie ->
//            MovieListCard(
//                movieItem = movie,
//                modifier = Modifier.fillMaxWidth(),
//                onAction = onAction
//            )
//        }
//    }
}

@Composable
private fun ErrorScreen(
    error: String,
    modifier: Modifier = Modifier,
    onAction: (MovieListAction) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = error)
            Button(onClick = { onAction }) {
                Text(text = "Обновить")
            }
        }
    }
}

@Composable
private fun MovieListCard(
    modifier: Modifier = Modifier,
    movieItem: Movie,
    onAction: (MovieListAction) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { movieItem.kinopoiskId?.let { onAction(MovieListAction.ItemClickAction(it)) } }
    ) {
        Row {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movieItem.posterUrlPreview)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)     // ← включить кэш в памяти
                        .diskCachePolicy(CachePolicy.ENABLED)       // ← включить кэш на диске
                        .build(),
                    contentDescription = null,

                    modifier = Modifier
                        .height(150.dp)
                        .padding(start = 6.dp, top = 6.dp, bottom = 6.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        )
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset(10.dp, 11.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(color = Color.Green)
                        .size(21.dp, 17.dp)

                ) {
                    Text(
                        text = movieItem.ratingKinopoisk.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = movieItem.nameRu.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "${movieItem.nameOriginal ?: movieItem.nameRu}, ${movieItem.year}",
                    style = MaterialTheme.typography.labelLarge
                )

                Text(
                    text = buildString {
                        append(
                            movieItem.countries?.joinToString()
                        )
                        append(" • ")
                        append(
                            movieItem.genres?.joinToString()
                        )
                    },
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    MovieAppTheme {
        MovieListScreen(
            state = MovieListState(),
            onAction = {}
        )
    }
}