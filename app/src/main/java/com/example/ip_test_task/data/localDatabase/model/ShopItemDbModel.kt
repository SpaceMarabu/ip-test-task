package com.example.ip_test_task.data.localDatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class ShopItemDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val time: Long,
    val tags: List<String>,
    val amount: Int
)
