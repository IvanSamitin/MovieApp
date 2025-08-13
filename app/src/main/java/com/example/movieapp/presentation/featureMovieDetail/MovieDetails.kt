package com.example.movieapp.presentation.featureMovieDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
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
    Scaffold { padding ->
        when {
            state.loading -> {
                LoadingIndicator()
            }

            state.error != null -> {
                ErrorState(modifier = Modifier.padding(padding), onAction, state.error)
            }

            state.movieDetails != null -> {
                MovieCard(
                    movieItem = state.movieDetails,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(padding),
                )
            }
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
            Text(text = "Ошибка")
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
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(movieItem.posterUrl)
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
            Text(
                text = movieItem.nameRu.toString(),
                style = MaterialTheme.typography.headlineSmall,
            )
            Row {
                Text(
                    text = "${movieItem.ratingKinopoisk}, ",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = movieItem.ratingKinopoiskVoteCount.toString(),
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
                Text(
                    text = "${movieItem.filmLength} мин, ",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = "Рейтинг: ${movieItem.ratingMpaa}",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        Text(text = movieItem.description.toString())
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
