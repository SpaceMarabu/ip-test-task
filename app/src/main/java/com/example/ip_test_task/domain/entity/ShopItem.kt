package com.example.ip_test_task.domain.entity

data class ShopItem(
    val id: Int,
    val name: String,
    val time: Int,
    val tags: List<String>,
    val amount: Int
)
