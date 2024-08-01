package com.example.ip_test_task.presentation.main

import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.ip_test_task.domain.entity.ShopItem
import com.example.ip_test_task.domain.usecases.GetItemsUseCase
import com.example.ip_test_task.presentation.main.MainStore.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainViewUDFModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
    private val storeFactory: MainStoreFactory
) : ViewModel() {

    private val store = storeFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    val model: StateFlow<MainStore.State> = store.stateFlow

    fun search(query: String) = store.accept(Intent.Search(query))

    fun editClick(item: ShopItem) = store.accept(Intent.EditClick(item))

    fun editDoneClick() = store.accept(Intent.EditDone)

    fun editCancelClick() = store.accept(Intent.EditCancel)

    fun plusAmountClick() = store.accept(Intent.PlusItemAmount)

    fun minusAmountClick() = store.accept(Intent.MinusItemAmount)

    fun removeClick(item: ShopItem) = store.accept(Intent.RemoveClick(item))

    fun removeDone() = store.accept(Intent.RemoveDone)

    fun removeCancelClick() = store.accept(Intent.RemoveCancel)
}