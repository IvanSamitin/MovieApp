package com.example.movieapp.presentation.seasonOverviewFeature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
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