package com.example.ip_test_task.data.repository

import com.example.ip_test_task.data.localDatabase.StoreItemsDao
import com.example.ip_test_task.data.mappers.LocalMapper
import com.example.ip_test_task.domain.entity.ShopItem
import com.example.ip_test_task.domain.repository.ItemsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemsRepositoryImpl @Inject constructor(
    private val storeItemsDao: StoreItemsDao,
    private val mapper: LocalMapper
) : ItemsRepository {

    override fun getItems() =
        storeItemsDao.getItems().map { itemsList ->
            val notNullableItemsList = itemsList ?: listOf()
            notNullableItemsList.map { item ->
                mapper.shopItemDbModelToEntity(item)
            }
        }


    override suspend fun editItem(item: ShopItem) {
        val itemModel = mapper.shopItemToDbModel(item)
        storeItemsDao.editItem(itemModel)
    }

    override suspend fun removeItem(item: ShopItem) {
        val itemModel = mapper.shopItemToDbModel(item)
        storeItemsDao.removeItem(itemModel)
    }


}