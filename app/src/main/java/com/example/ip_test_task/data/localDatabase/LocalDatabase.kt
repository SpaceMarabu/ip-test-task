package com.example.ip_test_task.data.localDatabase

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ip_test_task.data.localDatabase.model.ShopItemDbModel

@Database(entities = [ShopItemDbModel::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun itemsDao(): StoreItemsDao

    companion object {

        private var INSTANCE: LocalDatabase? = null
        private var LOCK = Any()
        private const val DB_NAME = "data.db"

        fun getInstance(application: Application): LocalDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    LocalDatabase::class.java,
                    DB_NAME
                )
                    .createFromAsset("init_db.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }
}