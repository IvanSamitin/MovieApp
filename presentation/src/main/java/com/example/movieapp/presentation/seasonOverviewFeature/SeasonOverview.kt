package com.example.movieapp.presentation.seasonOverviewFeature

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.domain.model.Season
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.ErrorState
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import org.koin.androidx.compose.koinViewModel

@Composable
fun SeasonOverviewRoot(
    viewModel: SeasonOverviewViewModel = koinViewModel<SeasonOverviewViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SeasonOverviewScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SeasonOverviewScreen(
    state: SeasonOverviewState,
    onAction: (SeasonOverviewAction) -> Unit,
) {
    when {
        state.loading -> LoadingIndicator()
        state.error.isNotBlank() -> ErrorState(
            onClick = { onAction(SeasonOverviewAction.Retry) },
            errorText = state.error
        )

        state.season.isNotEmpty() -> SeasonList(state = state, onAction = onAction)
        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Информация не найдена", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun SeasonList(
    modifier: Modifier = Modifier,
    state: SeasonOverviewState,
    onAction: (SeasonOverviewAction) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        state.season.forEach { season ->
            stickyHeader {
                Header(text = "${season.number} сезон", onAction = onAction)
            }
            items(season.episodes) { episode ->
                ListItem(
                    headlineContent = {
                        Text(text = "${episode.episodeNumber} ${episode.nameRu ?: "серия"}")
                    },
                    supportingContent = {
                        Column {
                            episode.nameEn?.let {
                                if (it.isNotBlank()) {
                                    Text(text = it)
                                }
                            }

                        }
                    },
                    overlineContent = {
                        episode.releaseDate?.let {
                            Text(text = it)
                        }
                    },
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    text: String,
    onAction: (SeasonOverviewAction) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(2.dp)

    ) {
        IconButton(onClick = { onAction(SeasonOverviewAction.NavigateBack) }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun HeaderPreview() {
    Header(text = "1", onAction = {})
}

@Preview
@Composable
private fun Preview() {
    MovieAppTheme {
        SeasonOverviewScreen(
            state = SeasonOverviewState(),
            onAction = {}
        )
    }
}