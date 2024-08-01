package com.example.ip_test_task.presentation.main

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ip_test_task.di.getApplicationComponent

@Composable
fun MainScreen() {

    val component = getApplicationComponent()
    val viewModel: MainViewUDFModel = viewModel(factory = component.getViewModelFactory())

//    val model =
}