package com.example.movieapp.presentation.featureHome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
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
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.List

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onItemClick: (movieId: Int) -> Unit,
    onCategoryClick: (movieCategory: MovieCategory) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is HomeAction.ItemClickAction -> onItemClick(action.movieId)
                is HomeAction.OnCategoryClick -> onCategoryClick(action.movieCategory)
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    val scrollState = rememberScrollState()

    when {
        state.loading -> {
            LoadingIndicator()
        }

        state.error != null -> {
            ErrorStateHome(modifier = Modifier.padding(), onAction = onAction, state.error)
        }

        state.movieCollection != null -> {
            Scaffold { innerPadding ->
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(innerPadding)
                ) {
                    PopularNow(
                        listMovie = state.movieCollection[MovieCategory.TOP_POPULAR_ALL]
                            ?: emptyList(),
                        onAction = onAction,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(Modifier.padding(top = 12.dp))
                    RowList(
                        listMovie = state.movieCollection[MovieCategory.TOP_250_MOVIES]
                            ?: emptyList(),
                        listDescription = "250 лучших фильмов кинопоиска.",
                        onAction = onAction,
                        movieCategory = MovieCategory.TOP_250_MOVIES,
                    )
                    RowList(
                        listMovie = state.movieCollection[MovieCategory.TOP_250_TV_SHOWS]
                            ?: emptyList(),
                        listDescription = "250 лучших сериалов кинопоиска.",
                        onAction = onAction,
                        movieCategory = MovieCategory.TOP_250_TV_SHOWS,
                    )
                    RowList(
                        listMovie = state.movieCollection[MovieCategory.COMICS_THEME]
                            ?: emptyList(),
                        listDescription = "Лучшие фильмы по комиксам.",
                        onAction = onAction,
                        movieCategory = MovieCategory.COMICS_THEME,
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorStateHome(
    modifier: Modifier = Modifier,
    onAction: (HomeAction) -> Unit,
    errorText: String,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Ошибка")
            Button(onClick = { onAction }) {
                Text(text = "Обновить")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PopularNow(
    modifier: Modifier = Modifier,
    listMovie: List<Movie>,
    onAction: (HomeAction) -> Unit,
) {
    Column(modifier = modifier) {
        Text(text = "Самые популярные фильмы", style = MaterialTheme.typography.headlineMedium)
        HorizontalUncontainedCarousel(
            state = remember(listMovie.size) {
                CarouselState(
                    itemCount = { listMovie.size },
                    currentItem = 0
                )
            },
            itemWidth = 250.dp,
        ) { movie ->
            val item = listMovie[movie]
            CarouselItem(movieItem = item, onAction = onAction)
        }
    }

}

@Composable
private fun CarouselItem(
    modifier: Modifier = Modifier,
    movieItem: Movie,
    onAction: (HomeAction) -> Unit,
) {
    Card(modifier = modifier.clickable {
        movieItem.kinopoiskId?.let { onAction(HomeAction.ItemClickAction(it)) }
    }) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .placeholder(R.drawable.f1_poster)
                    .data(movieItem.posterUrl)
                    .crossfade(true)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
            contentDescription = null,
            modifier =
                Modifier
                    .height(350.dp)
                    .clip(
                        RoundedCornerShape(12.dp),
                    ),
        )
    }
}

@Composable
private fun RowList(
    modifier: Modifier = Modifier,
    listMovie: List<Movie>,
    listDescription: String,
    onAction: (HomeAction) -> Unit,
    movieCategory: MovieCategory,

    ) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = listDescription, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            Text(text = "Все", modifier = Modifier.clickable {
                onAction(HomeAction.OnCategoryClick(movieCategory))
            })
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(listMovie) { movie ->
                RowMovieCard(movieItem = movie, onAction = onAction)
            }
            item {
                LoadMoreCard(onAction = onAction, movieCategory = movieCategory)
            }
        }
    }

}

@Composable
private fun LoadMoreCard(
    modifier: Modifier = Modifier,
    onAction: (HomeAction) -> Unit,
    movieCategory: MovieCategory,
) {
    Card(modifier = modifier.clickable { onAction(HomeAction.OnCategoryClick(movieCategory = movieCategory)) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .width(130.dp)
                .height(235.dp),
        ) {
            Icon(imageVector = Icons.Sharp.ArrowForward, contentDescription = null)
            Text(text = "Показать еще", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun RowMovieCard(
    modifier: Modifier = Modifier,
    movieItem: Movie,
    onAction: (HomeAction) -> Unit,
) {
    Card {
        Column(
            modifier = modifier
                .width(130.dp)
                .height(235.dp)
                .clickable {
                    movieItem.kinopoiskId?.let { onAction(HomeAction.ItemClickAction(it)) }
                }
        ) {
            Box {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(movieItem.posterUrlPreview)
                            .crossfade(true)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .padding(6.dp)
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
            Text(
                text = movieItem.nameRu.toString(),
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MovieAppTheme {
        HomeScreen(
            state = HomeState(loading = true),
            onAction = {}
        )
    }
}