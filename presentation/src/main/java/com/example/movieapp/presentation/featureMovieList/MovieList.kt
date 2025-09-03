package com.example.movieapp.presentation.featureMovieList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import org.koin.androidx.compose.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movieapp.presentation.ui.uiComponents.ErrorState
import com.example.movieapp.presentation.ui.uiComponents.MovieListCard
import com.example.movieapp.presentation.util.NavigationChannel
import com.example.movieapp.presentation.util.NavigationEvent
import kotlinx.coroutines.launch

@Composable
fun MovieListRoot(
    viewModel: MovieListViewModel = koinViewModel(),
) {
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    val categoryLabel = viewModel.categoryState.value.asString()

    MovieListScreen(
        pagingItems = pagingItems,
        categoryLabel = categoryLabel
    )
}

@Composable
fun MovieListScreen(
    pagingItems: LazyPagingItems<Movie>,
    categoryLabel: String
) {
    MoviePagingList(
        pagingItems = pagingItems,
        modifier = Modifier,
        categoryLabel = categoryLabel
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviePagingList(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<Movie>,
    categoryLabel: String
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Column(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = categoryLabel)
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        NavigationChannel.sendEvent(NavigationEvent.OnNavigateBack)
                    }
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            windowInsets = WindowInsets(top = 0.dp),
            scrollBehavior = scrollBehavior
        )
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(4.dp)
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
                        ErrorState(
                            modifier = Modifier,
                            onClick = { pagingItems.retry() },
                            errorText = "Отсутствует интернет",
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
                        ErrorState(
                            modifier = Modifier,
                            onClick = { pagingItems.retry() },
                            errorText = "Ошибка загрузки списка",
                        )
                    }
                }
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
