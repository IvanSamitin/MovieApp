package com.example.movieapp.presentation.featureMovieDetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.movieapp.R
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Type
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsState
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.ErrorState
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import com.example.movieapp.presentation.ui.uiComponents.shimmerEffect
import com.example.movieapp.presentation.util.formatNumber
import com.example.movieapp.presentation.util.formatNumberWithSpaces
import com.example.movieapp.presentation.util.getRatingColor
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
            val context = LocalContext.current
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopCard(
                        onAction = onAction,
                        context = context,
                        posterUrl = state.movieDetails?.posterUrl,
                        webUrl = state.movieDetails?.webUrl,
                    )
                    MovieData(movieDetails = state.movieDetails)
                    MovieActionButton(
                        isFavorite = state.isFavorite,
                        kinopoiskId = state.movieDetails?.kinopoiskId!!,
                        onAction = onAction,
                        context = context,
                        webUrl = state.movieDetails.webUrl
                    )
                }
                HorizontalDivider(
                    Modifier
                        .padding(vertical = 8.dp)
                )

                if (state.movieDetails.type == Type.TV_SERIES || state.movieDetails.type == Type.MINI_SERIES) {
                    Button(
                        onClick = { onAction(MovieDetailsAction.SeasonOverview(state.movieDetails.kinopoiskId!!)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    ) {
                        Text(text = "Сезоны и серии")
                    }
                }
                MovieDescription(description = state.movieDetails.description, onAction = onAction)
                MovieRating(movieDetails = state.movieDetails)
                DescriptionBottomShield(
                    description = state.movieDetails.description,
                    isOpen = state.isSheetOpen,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
fun MovieDescription(
    modifier: Modifier = Modifier,
    description: String?,
    onAction: (MovieDetailsAction) -> Unit,
) {
    Text(
        text = description ?: "Описание не добавлено",
        maxLines = 3,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.widthIn(max = 710.dp),
        overflow = TextOverflow.Ellipsis,
    )
    description?.let {
        TextButton(onClick = { onAction(MovieDetailsAction.SheetUpdate) }) {
            Text(text = "Читать полностью")
        }
    }
}

@Composable
private fun TopCard(
    modifier: Modifier = Modifier,
    posterUrl: String?,
    webUrl: String?,
    context: Context,
    onAction: (MovieDetailsAction) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
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
                    .data(posterUrl)
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
                    )
                    .widthIn(max = 350.dp),
        )
        IconButton(onClick = {
            val intent = Intent( ).apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, webUrl)
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

@Composable
private fun MovieActionButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    kinopoiskId: Int,
    webUrl: String?,
    context: Context,
    onAction: (MovieDetailsAction) -> Unit,
) {
    Row {
        IconButton(onClick = { onAction(MovieDetailsAction.AddToFav(kinopoiskId)) }) {
            if (isFavorite) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
            } else {
                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null)
            }

        }
        IconButton(onClick = {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(webUrl)
            }
            context.startActivity(intent)

        }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    }
}

@Composable
private fun MovieData(
    modifier: Modifier = Modifier,
    movieDetails: Movie?,
) {

    Text(
        text = movieDetails?.nameRu.toString(),
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "${movieDetails?.ratingKinopoisk ?: "Нет оценок"}, ",
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = "${movieDetails?.ratingKinopoiskVoteCount?.toInt()?.formatNumber()} ",
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = movieDetails?.nameOriginal ?: "",
            style = MaterialTheme.typography.labelLarge,
        )
    }
    Row {
        if (
            movieDetails?.type == Type.TV_SERIES &&
            movieDetails?.endYear != null &&
            movieDetails?.endYear != movieDetails.startYear
        ) {
            Text(
                text = "${movieDetails?.startYear} - ${movieDetails?.endYear}, ",
                style = MaterialTheme.typography.labelLarge,
            )
        } else {
            movieDetails?.year?.let {
                Text(
                    text = "$it, ",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        Text(
            text = movieDetails?.genres?.first() ?: "",
            style = MaterialTheme.typography.labelLarge,
        )
    }
    Row {
        Text(
            text = "${movieDetails?.countries?.get(0)}, ",
            style = MaterialTheme.typography.labelLarge,
        )
        movieDetails?.filmLength?.let {
            Text(
                text = "$it мин, ",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        movieDetails?.ratingAgeLimits?.let {
            Text(
                text = "Рейтинг: ${movieDetails?.ratingAgeLimits}",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DescriptionBottomShield(
    modifier: Modifier = Modifier,
    description: String?,
    isOpen: Boolean,
    onAction: (MovieDetailsAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    if (isOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                onAction(MovieDetailsAction.SheetUpdate)
            },
            modifier = modifier
        ) {
            Text(
                text = description ?: "Описание не добавлено",
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun MovieRating(modifier: Modifier = Modifier, movieDetails: Movie?) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.widthIn(max = 710.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        movieDetails?.ratingKinopoisk?.let {
            RatingCard(
                modifier = Modifier,
                color = getRatingColor(it.toUInt().toInt()),
                rating = it.toString(),
                title = "Рейтинг кинопоиска",
                ratingVote = movieDetails.ratingKinopoiskVoteCount!!
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.Center
        ) {
            movieDetails?.ratingImdb?.let {
                RatingCard(
                    rating = it.toString(),
                    title = "Рейтинг IMDb",
                    ratingVote = movieDetails.ratingImdbVoteCount!!
                )
            }
            movieDetails?.ratingFilmCritics?.let {
                RatingCard(
                    rating = it.toString(),
                    title = "Рейтинг критиков",
                    ratingVote = movieDetails.ratingFilmCriticsVoteCount!!
                )
            }
            movieDetails?.ratingGoodReview?.let {
                RatingCard(
                    rating = it.toString(),
                    title = "Рейтинг GoodReview",
                    ratingVote = movieDetails.ratingGoodReviewVoteCount!!
                )
            }
        }
    }
}

@Composable
private fun RatingCard(
    modifier: Modifier = Modifier,
    rating: String,
    color: Color? = null,
    ratingVote: Int,
    title: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding()
        ) {
            Text(
                text = rating,
                style = MaterialTheme.typography.displayMedium,
                color = color ?: Color.Unspecified,
                maxLines = 1
            )
            Spacer(Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = "${ratingVote.formatNumberWithSpaces()} оценок")
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
