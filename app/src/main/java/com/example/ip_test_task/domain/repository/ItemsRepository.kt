package com.example.ip_test_task.domain.repository

import com.example.ip_test_task.domain.entity.ShopItem
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    fun getItems(): Flow<List<ShopItem>>

    suspend fun editItem(item: ShopItem)

    suspend fun removeItem(item: ShopItem)
}