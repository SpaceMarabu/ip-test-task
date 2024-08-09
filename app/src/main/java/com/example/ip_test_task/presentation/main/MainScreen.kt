package com.example.ip_test_task.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ControlPoint
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ip_test_task.R
import com.example.ip_test_task.di.getApplicationComponent
import com.example.ip_test_task.domain.entity.ShopItem
import com.example.ip_test_task.presentation.utils.getScreenWidthDp
import com.example.ip_test_task.presentation.utils.toStringDate

@Composable
fun MainScreen() {

    val component = getApplicationComponent()
    val viewModel: MainViewUDFModel = viewModel(factory = component.getViewModelFactory())

    val model by viewModel.model.collectAsState()

    val shadowingBoxColor =
        if (model.isEditingMenuExpanded or model.isRemovingMenuExpanded) {
            Color.Black.copy(alpha = 0.5f)
        } else {
            Color.Transparent
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .zIndex(2f)
                .fillMaxSize()
                .background(shadowingBoxColor)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            ScreenHeader()
            SearchField(
                searchQuery = model.searchQuery,
                doSearch = { viewModel.search(it) },
                clearSearchField = { viewModel.clearSearch() }
            )

            when (val currentContentState = model.contentState) {
                is MainStore.State.ContentState.Content -> {
                    Content(
                        items = currentContentState.itemsFiltered,
                        currentItemAmount = model.currentItem?.amount ?: 0,
                        isEditingMenuExpanded = model.isEditingMenuExpanded,
                        isRemovingMenuExpanded = model.isRemovingMenuExpanded,
                        onRemoveClick = { viewModel.removeClick(it) },
                        onRemoveConfirm = { viewModel.removeDoneClick() },
                        onRemoveCancel = { viewModel.removeCancelClick() },
                        onEditClick = { viewModel.editClick(it) },
                        onEditConfirm = { viewModel.editDoneClick() },
                        onEditCancel = { viewModel.editCancelClick() },
                        onPlusAmountClick = { viewModel.plusAmountClick() },
                        onMinusAmountClick = { viewModel.minusAmountClick() }
                    )
                }

                MainStore.State.ContentState.Error -> {
                    Error()
                }

                MainStore.State.ContentState.Loading -> {
                    Loading()
                }

                MainStore.State.ContentState.Initial -> {}
            }
        }


    }
}

@Composable
private fun ScreenHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.items_list),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun Content(
    items: List<ShopItem>,
    currentItemAmount: Int,
    isEditingMenuExpanded: Boolean,
    isRemovingMenuExpanded: Boolean,
    onRemoveClick: (ShopItem) -> Unit,
    onRemoveConfirm: () -> Unit,
    onRemoveCancel: () -> Unit,
    onEditClick: (ShopItem) -> Unit,
    onPlusAmountClick: () -> Unit,
    onMinusAmountClick: () -> Unit,
    onEditConfirm: () -> Unit,
    onEditCancel: () -> Unit
) {
    if (isEditingMenuExpanded) {
        EditingDialog(
            titleIcon = Icons.Default.Settings,
            titleText = stringResource(id = R.string.items_amount),
            iconDescription = "items settings",
            onDismiss = { onEditCancel() },
            onConfirm = { onEditConfirm() }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ClickableIcon(
                    imageVector = Icons.Default.RemoveCircleOutline,
                    description = "decrease item amount"
                ) {
                    onMinusAmountClick()
                }
                Text(text = currentItemAmount.toString())
                ClickableIcon(
                    imageVector = Icons.Default.ControlPoint,
                    description = "increase item amount"
                ) {
                    onPlusAmountClick()
                }
            }
        }
    }

    if (isRemovingMenuExpanded) {
        EditingDialog(
            titleIcon = Icons.Default.Warning,
            iconDescription = "icon warning",
            titleText = stringResource(R.string.removing_item),
            onDismiss = { onRemoveCancel() },
            onConfirm = { onRemoveConfirm() },
            dismissText = stringResource(R.string.no),
            confirmText = stringResource(R.string.yes)
        ) {
            Row {
                Text(text = stringResource(R.string.confirm_deleting_question))
            }
        }
    }

    LazyColumn {
        items(items = items, key = { it.id }) { item ->
            CardShopItem(item = item,
                onEditClick = { onEditClick(it) },
                onRemoveClick = { onRemoveClick(it) })
        }
    }
}

@Composable
private fun EditingDialog(
    titleIcon: ImageVector,
    iconDescription: String,
    titleText: String,
    dismissText: String = stringResource(id = R.string.cancel),
    confirmText: String = stringResource(id = R.string.done),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable (() -> Unit),

    ) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            AlertButton(text = confirmText) {
                onConfirm()
            }
        },
        dismissButton = {
            AlertButton(text = dismissText) {
                onDismiss()
            }
        },
        icon = {
            Icon(
                imageVector = titleIcon,
                contentDescription = iconDescription,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        text = {
            content()
        }
    )
}

@Composable
private fun ClickableIcon(imageVector: ImageVector, description: String, onClick: () -> Unit) {
    Icon(
        modifier = Modifier.clickable {
            onClick()
        },
        imageVector = imageVector,
        contentDescription = description
    )
}

@Composable
private fun AlertButton(text: String, onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        text = text,
        color = MaterialTheme.colorScheme.outline
    )
}

@Composable
private fun SearchField(
    searchQuery: String,
    doSearch: (String) -> Unit,
    clearSearchField: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        label = { Text("Поиск товаров") },
        value = searchQuery,
        onValueChange = {
            doSearch(it)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = { clearSearchField() }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null
                )
            }
        },
        shape = MaterialTheme.shapes.extraSmall,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
            focusedSupportingTextColor = MaterialTheme.colorScheme.outline,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),
            cursorColor = MaterialTheme.colorScheme.outline
        ),
        singleLine = true
    )
}

@Preview
@Composable
private fun Error() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.something_wrong),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.try_reload),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun Loading() {
    CircularProgressIndicator()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CardShopItem(
    item: ShopItem,
    onEditClick: (ShopItem) -> Unit,
    onRemoveClick: (ShopItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleLarge
            )
            Row {
                Icon(
                    modifier = Modifier
                        .clickable {
                            onEditClick(item)
                        },
                    imageVector = Icons.Default.Edit,
                    contentDescription = "editing item",
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .clickable {
                            onRemoveClick(item)
                        },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "deleting item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        FlowRow(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            item.tags.forEach { tag ->
                AssistChip(
                    modifier = Modifier.padding(end = 4.dp),
                    onClick = {},
                    label = { Text(tag) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleWithData(
                title = stringResource(R.string.on_warehouse),
                data = item.amount.toString()
            )
            TitleWithData(
                title = stringResource(R.string.adding_date),
                data = item.time.toStringDate()
            )
        }

    }
}

@Composable
private fun TitleWithData(title: String, data: String) {
    Column(
        modifier = Modifier
            .width(getScreenWidthDp() / 2)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = data)
    }
}




















