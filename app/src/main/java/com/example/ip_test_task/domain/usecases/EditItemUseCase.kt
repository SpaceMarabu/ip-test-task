package com.example.ip_test_task.domain.usecases

import com.example.ip_test_task.domain.entity.ShopItem
import com.example.ip_test_task.domain.repository.ItemsRepository
import javax.inject.Inject

class EditItemUseCase  @Inject constructor(
    private val repository: ItemsRepository
) {

    suspend fun editItem(item: ShopItem) {
        return repository.editItem(item)
    }

    suspend fun removeItem(item: ShopItem) {
        return repository.removeItem(item)
    }
}