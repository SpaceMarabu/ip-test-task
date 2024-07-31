package com.example.ip_test_task.data.localDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ip_test_task.data.localDatabase.model.ShopItemDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreItemsDao {

    @Query("SELECT * FROM items")
    fun getItems(): Flow<List<ShopItemDbModel>?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun editItem(itemDbModel: ShopItemDbModel)

    @Delete
    suspend fun removeItem(itemDbModel: ShopItemDbModel)
}