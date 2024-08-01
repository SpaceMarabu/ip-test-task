package com.example.ip_test_task.domain.usecases

import com.example.ip_test_task.domain.entity.ShopItem
import com.example.ip_test_task.domain.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUseCase  @Inject constructor(
    private val repository: ItemsRepository
) {

    fun getItemsFlow(): Flow<List<ShopItem>> {
        return repository.getItems()
    }
}