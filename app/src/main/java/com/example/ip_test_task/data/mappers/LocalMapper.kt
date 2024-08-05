package com.example.ip_test_task.data.mappers

import com.example.ip_test_task.data.localDatabase.model.ShopItemDbModel
import com.example.ip_test_task.domain.entity.ShopItem
import javax.inject.Inject

class LocalMapper @Inject constructor() {

    fun shopItemDbModelToEntity(model: ShopItemDbModel) = ShopItem(
        id = model.id,
        name = model.name,
        time = model.time,
        tags = model.tags,
        amount = model.amount
    )

    fun shopItemToDbModel(entity: ShopItem) = ShopItemDbModel(
        id = entity.id,
        name = entity.name,
        time = entity.time,
        tags = entity.tags,
        amount = entity.amount
    )
}