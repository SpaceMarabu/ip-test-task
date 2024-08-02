package com.example.ip_test_task.presentation.utils

import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

fun Long.toStringDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

@Composable
fun getScreenWidthDp(): Dp {
    val displayMetrics: DisplayMetrics = LocalContext.current.resources.displayMetrics
    return (displayMetrics.widthPixels / displayMetrics.density).dp
}