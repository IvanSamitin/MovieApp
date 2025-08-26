package com.example.movieapp.presentation.featureMovieList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.movieapp.presentation.util.NavigationChannel
import com.example.movieapp.presentation.util.NavigationEvent
import kotlinx.coroutines.launch

@Composable
fun MovieListRoot(
    viewModel: MovieListViewModel = koinViewModel(),
) {
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    MovieListScreen(
        pagingItems = pagingItems,
    )
}

@Composable
fun MovieListScreen(
    pagingItems: LazyPagingItems<Movie>,
) {
    MoviePagingList(
        pagingItems = pagingItems,
        modifier = Modifier,
    )

}

@Composable
private fun MoviePagingList(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<Movie>,
) {
    val scope = rememberCoroutineScope()
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
                onItemClick = { id ->
                    scope.launch {
                        NavigationChannel.sendEvent(NavigationEvent.OnItemClick(id))
                    }
                }
            )
        }
        pagingItems.loadState.refresh.let { loadState ->
            if (loadState is LoadState.Error) {
                item {
                    ErrorScreen(
                        modifier = Modifier.fillMaxSize(),
                        onAction = { pagingItems.retry() },
                        error = "Отсутствует интернет",
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
                        error = "Ошибка загрузки списка",
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
            Spacer(Modifier.height(8.dp))
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
