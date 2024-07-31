package com.example.ip_test_task.di

import android.app.Application
import com.example.ip_test_task.data.localDatabase.StoreItemsDao
import com.example.ip_test_task.data.localDatabase.LocalDatabase
import com.example.ip_test_task.data.repository.ItemsRepositoryImpl
import com.example.ip_test_task.domain.repository.ItemsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: ItemsRepositoryImpl): ItemsRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideItemsDao(
            application: Application
        ): StoreItemsDao {
            return LocalDatabase.getInstance(application).itemsDao()
        }
    }
}
