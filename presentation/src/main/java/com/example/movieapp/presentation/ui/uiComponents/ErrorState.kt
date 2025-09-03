package com.example.movieapp.presentation.ui.uiComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieapp.presentation.featureMovieDetail.MovieDetailsAction

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    errorText: String,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Ошибка: $errorText")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onClick) {
                Text(text = "Обновить")
            }
        }
    }
}