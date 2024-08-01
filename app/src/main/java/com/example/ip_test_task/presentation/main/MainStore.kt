package com.example.ip_test_task.presentation.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.ip_test_task.domain.entity.ShopItem
import com.example.ip_test_task.domain.usecases.EditItemUseCase
import com.example.ip_test_task.domain.usecases.GetItemsUseCase
import com.example.ip_test_task.presentation.main.MainStore.Intent
import com.example.ip_test_task.presentation.main.MainStore.Label
import com.example.ip_test_task.presentation.main.MainStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MainStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class Search(val query: String) : Intent

        data class EditClick(val item: ShopItem) : Intent

        data object EditDone : Intent

        data object EditCancel : Intent

        data object PlusItemAmount : Intent

        data object MinusItemAmount : Intent

        data class RemoveClick(val item: ShopItem) : Intent

        data object RemoveDone : Intent

        data object RemoveCancel : Intent
    }

    data class State(
        val contentState: ContentState,
        val searchQuery: String = "",
        val isEditingMenuOpen: Boolean = false,
        val isRemovingMenuOpen: Boolean = false,
        val currentItem: ShopItem? = null
    ) {

        sealed interface ContentState {

            data object Loading : ContentState

            data object Error : ContentState

            data object Initial : ContentState

            data class Content(val items: List<ShopItem>) : ContentState
        }
    }

    sealed interface Label
}

class MainStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getItemsUseCase: GetItemsUseCase,
    private val editItemUseCase: EditItemUseCase
) {

    fun create(): MainStore =
        object : MainStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainStore",
            initialState = State(contentState = State.ContentState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object Loading : Action

        data object Error : Action

        data class DataLoaded(val items: List<ShopItem>) : Action
    }

    private sealed interface Msg {

        data object Loading : Msg

        data object Error : Msg

        data class DataLoaded(val items: List<ShopItem>) : Msg

        data class Search(val query: String) : Msg

        data class EditClick(val item: ShopItem) : Msg

        data object EditDone : Msg

        data object EditCancel : Msg

        data object PlusItemAmount : Msg

        data object MinusItemAmount : Msg

        data class RemoveClick(val item: ShopItem) : Msg

        data object RemoveDone : Msg

        data object RemoveCancel : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            try {
                scope.launch {
                    dispatch(Action.Loading)
                    getItemsUseCase.getItemsFlow()
                        .collect { itemList ->
                            dispatch(Action.DataLoaded(itemList))
                        }
                }
            } catch (e: Exception) {
                dispatch(Action.Error)
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.EditCancel -> {
                    dispatch(Msg.EditCancel)
                }

                is Intent.EditClick -> {
                    dispatch(Msg.EditClick(intent.item))
                }

                Intent.EditDone -> {
                    val currentItem = getState().currentItem
                    if (currentItem != null) {
                        scope.launch {
                            editItemUseCase.editItem(currentItem)
                        }
                    }
                    dispatch(Msg.EditDone)
                }

                Intent.PlusItemAmount -> {
                    dispatch(Msg.PlusItemAmount)
                }

                Intent.MinusItemAmount -> {
                    dispatch(Msg.MinusItemAmount)
                }

                Intent.RemoveCancel -> {
                    dispatch(Msg.RemoveCancel)
                }

                is Intent.RemoveClick -> {
                    dispatch(Msg.RemoveClick(intent.item))
                }

                Intent.RemoveDone -> {
                    val currentItem = getState().currentItem
                    if (currentItem != null) {
                        scope.launch {
                            editItemUseCase.removeItem(currentItem)
                        }
                    }
                    dispatch(Msg.RemoveDone)
                }

                is Intent.Search -> {
                    dispatch(Msg.Search(intent.query))
                }

            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.DataLoaded -> {
                    dispatch(Msg.DataLoaded(action.items))
                }

                Action.Error -> {
                    dispatch(Msg.Error)
                }

                Action.Loading -> {
                    dispatch(Msg.Loading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.DataLoaded -> {
                    this.copy(
                        contentState = State.ContentState.Content(
                            items = msg.items.filter { it.name.contains(this.searchQuery) }
                        )
                    )
                }

                Msg.EditCancel -> {
                    this.copy(
                        isEditingMenuOpen = false,
                        currentItem = null
                    )
                }

                is Msg.EditClick -> {
                    this.copy(
                        isEditingMenuOpen = true,
                        currentItem = msg.item
                    )
                }

                Msg.EditDone -> {
                    this.copy(
                        isEditingMenuOpen = false,
                        currentItem = null
                    )
                }

                Msg.Error -> {
                    this.copy(
                        contentState = State.ContentState.Error
                    )
                }

                Msg.Loading -> {
                    this.copy(
                        contentState = State.ContentState.Loading
                    )
                }

                Msg.MinusItemAmount -> {
                    val currentItem = this.currentItem
                    val newItem = currentItem?.copy(
                        amount = maxOf(currentItem.amount - 1, 0)
                    )
                    this.copy(
                        currentItem = newItem
                    )
                }

                Msg.PlusItemAmount -> {
                    val currentItem = this.currentItem
                    val newItem = currentItem?.copy(
                        amount = maxOf(currentItem.amount + 1, 0)
                    )
                    this.copy(
                        currentItem = newItem
                    )
                }

                Msg.RemoveCancel -> {
                    this.copy(
                        isRemovingMenuOpen = false,
                        currentItem = null
                    )
                }

                is Msg.RemoveClick -> {
                    this.copy(
                        isRemovingMenuOpen = true,
                        currentItem = msg.item
                    )
                }

                Msg.RemoveDone -> {
                    this.copy(
                        isRemovingMenuOpen = false,
                        currentItem = null
                    )
                }

                is Msg.Search -> {
                    val currentContentState = this.contentState
                    if (currentContentState is State.ContentState.Content) {
                        this.copy(
                            searchQuery = msg.query,
                            contentState = currentContentState.copy(
                                items = currentContentState.items.filter {
                                    it.name.contains(msg.query)
                                }
                            )
                        )
                    } else {
                        this.copy(
                            searchQuery = msg.query
                        )
                    }

                }
            }
    }
}
