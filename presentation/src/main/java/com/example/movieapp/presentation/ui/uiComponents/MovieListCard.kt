package com.example.movieapp.presentation.ui.uiComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.movieapp.domain.model.Movie

@Composable
fun MovieListCard(
    modifier: Modifier = Modifier,
    movieItem: Movie,
    onItemClick: (movieId: Int) -> Unit,
) {
    Card(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .height(150.dp)
                .clickable {
                    movieItem.kinopoiskId?.let {
                        onItemClick(it)
                    }
                },
    ) {
        Row {
            Box {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(movieItem.posterUrlPreview)
                            .crossfade(true)
                            .placeholder(com.example.movieapp.R.drawable.loading_placeholder)
                            .error(com.example.movieapp.R.drawable.error_placeholder)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .height(150.dp)
                            .padding(start = 6.dp, top = 6.dp, bottom = 6.dp)
                            .clip(
                                RoundedCornerShape(8.dp),
                            ),
                )
                movieItem.ratingKinopoisk?.let { rating ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =
                            Modifier
                                .offset(10.dp, 11.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(color = Color.Green)
                                .size(21.dp, 17.dp),
                    ) {
                        Text(
                            text = rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                        )
                    }
                }

            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = movieItem.nameRu.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${movieItem.nameOriginal ?: movieItem.nameRu}, ${movieItem.year}",
                    style = MaterialTheme.typography.labelMedium,

                )

                Text(
                    text =
                        buildString {
                            append(
                                movieItem.countries?.joinToString(),
                            )
                            append(" • ")
                            append(
                                movieItem.genres?.joinToString(),
                            )
                        },
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}