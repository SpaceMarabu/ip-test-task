package com.example.ip_test_task.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.ip_test_task.presentation.main.MainScreen
import com.example.ip_test_task.presentation.theme.IptesttaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = Color(0xFFB1DCFB).toArgb()
        window.navigationBarColor = Color(0xFFB1DCFB).toArgb()
        super.onCreate(savedInstanceState)

        setContent {
            IptesttaskTheme {
                MainScreen()
            }
        }
    }
}