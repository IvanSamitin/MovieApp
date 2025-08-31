package com.example.movieapp.presentation.ui.uiComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex

@Composable
internal fun DialogBox(onclickOutside: () -> Unit, content: @Composable () -> Unit) {
    Dialog(
        onDismissRequest = { },
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f),
            contentAlignment = Alignment.Center
        ) {
            Dialog(onDismissRequest = onclickOutside) {
                content()
            }

        }
    }
}