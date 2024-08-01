package com.example.ip_test_task.data.localDatabase

import androidx.room.TypeConverter
import com.example.ip_test_task.data.localDatabase.model.ShopItemDbModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters {

    @TypeConverter
    fun fromItemsList(items: List<ShopItemDbModel>?): String? {
        if (items == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<ShopItemDbModel>>() {}.type
        return gson.toJson(items, type)
    }

    @TypeConverter
    fun toItemsList(items: String?): List<ShopItemDbModel?>? {
        if (items == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<ShopItemDbModel>>() {}.type
        return gson.fromJson(items, type)
    }

    @TypeConverter
    fun fromTagsList(tags: List<String>): String {
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<String>>() {}.type
        return gson.toJson(tags, type)
    }

    @TypeConverter
    fun toTagsList(tags: String): List<String> {
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<String>>() {}.type
        return gson.fromJson(tags, type)
    }
}