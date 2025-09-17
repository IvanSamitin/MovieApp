package com.example.movieapp.presentation.util

import androidx.compose.ui.graphics.Color
import java.text.NumberFormat
import java.util.Locale

fun getRatingColor(number: Int): Color =
    when {
        number in 0..4 -> Color.Red
        number in 5..6 -> Color.DarkGray
        number in 7..10 -> Color(0xFF228B22)
        else -> Color.Unspecified
    }

fun Int.formatNumber(): String = "${this / 1000}K"


fun Int.formatNumberWithSpaces(): String =
    NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
