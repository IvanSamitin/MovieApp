package com.example.movieapp.presentation.featureMovieDetail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
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
            ErrorState(modifier = Modifier, onAction, state.error)
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

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    onAction: (MovieDetailsAction) -> Unit,
    errorText: String,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Ошибка $errorText")
            Button(onClick = { onAction(MovieDetailsAction.Update) }) {
                Text(text = "Обновить")
            }
        }
    }
}

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
                Text(
                    text = "${movieItem.year}, ",
                    style = MaterialTheme.typography.labelLarge,
                )
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
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        Text(text = movieItem.description ?: "Описание не добавлено")
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
