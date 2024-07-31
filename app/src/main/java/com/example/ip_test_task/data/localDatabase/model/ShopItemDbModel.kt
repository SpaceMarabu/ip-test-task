package com.example.ip_test_task.data.localDatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ShopItemDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val time: Int,
    val tags: String,
    val amount: Int
)
