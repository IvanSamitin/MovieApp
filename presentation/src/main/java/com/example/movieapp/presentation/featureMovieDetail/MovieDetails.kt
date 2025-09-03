package com.example.movieapp.presentation.featureMovieDetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Type
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.ErrorState
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsRoot(viewModel: MovieDetailsViewModel = koinViewModel<MovieDetailsViewModel>()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MovieDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun MovieDetailsScreen(
    state: MovieDetailsState,
    onAction: (MovieDetailsAction) -> Unit,
) {

    when {
        state.loading -> {
            LoadingIndicator()
        }

        state.error != null -> {
            ErrorState(
                modifier = Modifier,
                onClick = { onAction(MovieDetailsAction.Update) },
                state.error.asString()
            )
        }

        state.movieDetails != null -> {
            MovieCard(
                movieItem = state.movieDetails,
                onAction = onAction,
                state = state,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieCard(
    modifier: Modifier = Modifier,
    movieItem: Movie,
    state: MovieDetailsState,
    onAction: (MovieDetailsAction) -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    onAction(MovieDetailsAction.NavigateBack)
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(movieItem.posterUrl)
                            .crossfade(true)
                            .placeholder(R.drawable.main_placeholder)
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
                IconButton(onClick = {
                    val intent = Intent(
                        Intent.ACTION_SEND
                    ).apply {
                        putExtra(Intent.EXTRA_TEXT, movieItem.webUrl)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(
                        intent, "Поделится фильмом:"
                    )
                    context.startActivity(shareIntent)
                }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null)
                }
            }

            Text(
                text = movieItem.nameRu.toString(),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "${movieItem.ratingKinopoisk ?: "Нет оценок"}, ",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = "${movieItem.ratingKinopoiskVoteCount} ",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = movieItem.nameOriginal ?: "",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Row {
                if (
                    movieItem.type == Type.TV_SERIES &&
                    movieItem.endYear != null &&
                    movieItem.endYear != movieItem.startYear
                ) {
                    Text(
                        text = "${movieItem.startYear} - ${movieItem.endYear}, ",
                        style = MaterialTheme.typography.labelLarge,
                    )
                } else {
                    movieItem.year?.let {
                        Text(
                            text = "$it, ",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }

                Text(
                    text = movieItem.genres?.first() ?: "",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Row {
                Text(
                    text = "${movieItem.countries?.get(0)}, ",
                    style = MaterialTheme.typography.labelLarge,
                )
                movieItem.filmLength?.let {
                    Text(
                        text = "$it мин, ",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                movieItem.ratingAgeLimits?.let {
                    Text(
                        text = "Рейтинг: ${movieItem.ratingAgeLimits}",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            Row {
                IconButton(onClick = { onAction(MovieDetailsAction.AddToFav(movieItem.kinopoiskId!!)) }) {
                    if (state.isFavorite) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                    } else {
                        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null)
                    }

                }
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW)

                    intent.data = Uri.parse(movieItem.webUrl)
                    context.startActivity(intent)

                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        val sheetState = rememberModalBottomSheetState()

        if (movieItem.type == Type.TV_SERIES || movieItem.type == Type.MINI_SERIES) {
            Button(
                onClick = { onAction(MovieDetailsAction.SeasonOverview(movieItem.kinopoiskId!!)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(text = "Сезоны и серии")
            }
        }

        Text(
            text = movieItem.description ?: "Описание не добавлено",
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        movieItem.description?.let {
            TextButton(onClick = { onAction(MovieDetailsAction.SheetUpdate) }) {
                Text(text = "Читать полностью")
            }
        }


        if (state.isSheetOpen) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    onAction(MovieDetailsAction.SheetUpdate)
                },
            ) {
                Text(
                    text = movieItem.description ?: "Описание не добавлено",
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MovieAppTheme {
        MovieDetailsScreen(
            state = MovieDetailsState(movieDetails = null, error = null, loading = true),
            onAction = {},
        )
    }
}
