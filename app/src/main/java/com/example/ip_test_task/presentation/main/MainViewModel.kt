package com.example.ip_test_task.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ip_test_task.domain.usecases.GetAllItemsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getAllItemsUseCase.getItemsFlow().collect {
                it
            }
        }
    }
}