package com.example.movieapp.presentation.searchFeature

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Type
import com.example.movieapp.presentation.ui.theme.MovieAppTheme
import com.example.movieapp.presentation.ui.uiComponents.DialogBox
import com.example.movieapp.presentation.ui.uiComponents.DropDownMenuSeacrh
import com.example.movieapp.presentation.ui.uiComponents.ErrorState
import com.example.movieapp.presentation.ui.uiComponents.LoadingIndicator
import com.example.movieapp.presentation.ui.uiComponents.MovieList
import com.example.movieapp.presentation.util.asUiText
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchRoot(
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val search by viewModel.searchText.collectAsStateWithLifecycle()
    SearchScreen(
        state = state,
        search = search,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    search: String,
    onAction: (SearchAction) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackHandler(enabled = state.isSearched) {
            onAction(SearchAction.BackHandler)
        }

        LaunchedEffect(state.scrollToPosition) {
            state.scrollToPosition?.let { position ->
                if (state.listMovie.isNotEmpty()) {
                    lazyListState.scrollToItem(position)
                    onAction(SearchAction.OnScrollCompleted)
                }
            }
        }
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            state = state,
            search = search,
            onAction = onAction,
        )
        when {
            state.error != null -> {
                ErrorState(
                    modifier = Modifier.padding(),
                    onClick = { onAction(SearchAction.OnError) },
                    state.error.asString()
                )
            }

            state.loading -> {
                LoadingIndicator()
            }
        }
        MovieList(
            movieItems = state.listMovie,
            onItemClick = { onAction(SearchAction.OnItemClick(it)) },
            lazyListState = lazyListState
        )

        if (state.isDialogOpen) {
            DialogBox(onclickOutside = { onAction(SearchAction.ShowFilters) }) {
                Card {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Фильтры", style = MaterialTheme.typography.displaySmall)
                        Spacer(Modifier.height(12.dp))
                        DropDownMenuSeacrh(
                            list = Type.values().toList(),
                            label = "Тип контента",
                            text = state.typeMenuValue?.asUiText()?.asString()
                        ) {
                            onAction(SearchAction.OnTypeChange(it))
                        }
                        Spacer(Modifier.height(12.dp))
                        DropDownMenuSeacrh(
                            list = Order.values().toList(),
                            label = "Сортировка",
                            text = state.orderMenuValue?.asUiText()?.asString()
                        ) {
                            onAction(SearchAction.OnOrderChange(it))
                        }
                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Оценка: от ${
                                String.format(
                                    "%.1f",
                                    state.sliderState.start
                                )
                            } до ${String.format("%.1f", state.sliderState.endInclusive)}"
                        )

                        RangeSlider(
                            value = state.sliderState,
                            onValueChange = { onAction(SearchAction.OnSliderChange(it)) },
                            valueRange = 0f..10f,
                        )

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = {
                                onAction(SearchAction.OnSearchClick)
                                onAction(SearchAction.ShowFilters)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Показать")
                        }
                        Spacer(Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = {
                                onAction(SearchAction.ShowFilters)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Отмена")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    state: SearchState,
    search: String,
    onAction: (SearchAction) -> Unit,
) {

    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = search,
        onValueChange = { onAction(SearchAction.OnSearchTextChange(it)) },
        placeholder = { Text(text = "Поиск") },
        trailingIcon = {
            IconButton(onClick = { onAction(SearchAction.ShowFilters) }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        },
        leadingIcon = {
            if (state.isSearched) {
                IconButton(onClick = {
                    onAction(SearchAction.BackHandler)
                }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
                }
            } else {
                IconButton(onClick = {
                    onAction(SearchAction.OnSearchClick)
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            }
        },
        supportingText = {
            AnimatedVisibility(visible = state.isInputTextError) {
                Text(text = state.searchErrorText)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onAction(SearchAction.OnSearchClick)
                focusManager.clearFocus()
            }
        ),
        isError = state.isInputTextError,
        singleLine = true,
        modifier = modifier.padding(horizontal = 8.dp)
    )
}

//@Preview
//@Composable
//private fun Preview() {
//    MovieAppTheme {
//        SearchScreen(
//            state = SearchState(),
//
//            onAction = {}
//        )
//    }
//}