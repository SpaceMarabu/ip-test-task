package com.example.ip_test_task.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ip_test_task.di.getApplicationComponent
import com.example.ip_test_task.presentation.main.MainScreen
import com.example.ip_test_task.presentation.main.MainViewUDFModel
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