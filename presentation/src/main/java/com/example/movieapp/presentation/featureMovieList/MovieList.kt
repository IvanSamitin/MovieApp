package com.example.movieapp.presentation.featureMovieList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import org.koin.androidx.compose.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun MovieListRoot(
    viewModel: MovieListViewModel = koinViewModel(),
    onItemClick: (movieId: Int) -> Unit,
) {

    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    MovieListScreen(
        pagingItems = pagingItems,
        onItemClick = onItemClick
    )
}

@Composable
fun MovieListScreen(
    pagingItems: LazyPagingItems<Movie>,
    onItemClick: (movieId: Int) -> Unit,
) {

    Scaffold(
        topBar = { TopBar() },
    ) { padding ->
        MovieList(
            pagingItems = pagingItems,
            modifier =
                Modifier
                    .padding(padding),
            onItemClick = onItemClick,
        )
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
    pagingItems: LazyPagingItems<Movie>,
    onItemClick: (movieId: Int) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(pagingItems.itemCount) { movie ->
            val item = pagingItems[movie]
            MovieListCard(
                movieItem = item!!,
                modifier = Modifier.fillMaxWidth(),
                onItemClick = onItemClick
            )
        }
        pagingItems.loadState.refresh.let { loadState ->
            if (loadState is LoadState.Error) {
                item {
                    ErrorScreen(
                        modifier = Modifier,
                        onAction = { pagingItems.retry() },
                        error = loadState.error.message ?: "",
                    )
                }
            } else if (loadState is LoadState.Loading) {
                item {
                    LoadingIndicator()
                }
            }
        }
        pagingItems.loadState.append.let { loadState ->
            if (loadState is LoadState.Error) {
                item {
                    ErrorScreen(
                        modifier = Modifier,
                        onAction = { pagingItems.retry() },
                        error = "ошибка",
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorScreen(
    error: String,
    modifier: Modifier = Modifier,
    onAction: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = error)
            Button(onClick = onAction) {
                Text(text = "Обновить")
            }
        }
    }
}

@Composable
private fun MovieListCard(
    modifier: Modifier = Modifier,
    movieItem: Movie,
    onItemClick: (movieId: Int) -> Unit,
) {
    Card(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    movieItem.kinopoiskId?.let {
                        onItemClick(it)
                    }
                },
    ) {
        Row {
            Box {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(movieItem.posterUrlPreview)
                            .crossfade(true)
                            .memoryCachePolicy(CachePolicy.ENABLED) // ← включить кэш в памяти
                            .diskCachePolicy(CachePolicy.ENABLED) // ← включить кэш на диске
                            .build(),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .height(150.dp)
                            .padding(start = 6.dp, top = 6.dp, bottom = 6.dp)
                            .clip(
                                RoundedCornerShape(8.dp),
                            ),
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier =
                        Modifier
                            .offset(10.dp, 11.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(color = Color.Green)
                            .size(21.dp, 17.dp),
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
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = "${movieItem.nameOriginal ?: movieItem.nameRu}, ${movieItem.year}",
                    style = MaterialTheme.typography.labelLarge,
                )

                Text(
                    text =
                        buildString {
                            append(
                                movieItem.countries?.joinToString(),
                            )
                            append(" • ")
                            append(
                                movieItem.genres?.joinToString(),
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
//        MovieListScreen(
//            pagingItems = LazyPagingItems<Movie> ,
//            onItemClick = {},
//        )
    }
}
