package com.example.movieapp.presentation.featureMovieList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import org.koin.androidx.compose.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieapp.presentation.ui.uiComponents.MovieListCard

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
        MoviePagingList(
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
private fun MoviePagingList(
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
