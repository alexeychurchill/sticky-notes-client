@file:OptIn(ExperimentalMaterialApi::class)

package io.github.alexeychurchill.stickynotes.contacts.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.contacts.presentation.AddContactViewModel
import io.github.alexeychurchill.stickynotes.contacts.presentation.SearchContactResult.*
import io.github.alexeychurchill.stickynotes.core.model.User
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Medium
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular

@Composable
fun ModalAddContactScreen(
    modifier: Modifier = Modifier,
    viewModel: AddContactViewModel = viewModel(),
    modalState: ModalBottomSheetState = rememberModalBottomSheetState(initialValue = Hidden),
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = modalState,
        content = content,
        sheetContent = {
            Surface(modifier = Modifier.fillMaxHeight(fraction = 0.85f)) {
                val searchQuery by viewModel.searchQuery.collectAsState()
                val searchResult by viewModel.searchResults.collectAsState()
                Column(modifier = Modifier.fillMaxWidth()) {
                    SearchContactTextField(
                        value = searchQuery,
                        onChange = viewModel::onSearchQueryChange,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1.0f),
                    ) {
                        when (searchResult) {
                            is None, Empty -> { /** NO OP **/ }

                            Loading -> CircularProgressIndicator(
                                modifier = Modifier.align(Center)
                            )

                            is Items -> SearchUsersResult(
                                modifier = Modifier.fillMaxSize(),
                                items = (searchResult as Items).items,
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun SearchContactTextField(
    onSearch: (String) -> Unit = { },
    value: String,
    onChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Regular)
            .padding(top = Medium),
        value = value,
        onValueChange = onChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                    )
                }
            }
        },
        placeholder = {
            Text(text = stringResource(R.string.screen_add_contact_search_hint))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii,
            autoCorrect = false,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(value)
            },
        ),
    )
}

@Composable
private fun SearchUsersResult(
    modifier: Modifier = Modifier,
    onAdd: (User) -> Unit = { },
    requestedTo: Set<String> = emptySet(),
    items: List<User>,
) {
    LazyColumn(modifier = modifier) {
        items(items = items, key = User::id) { user ->
            ContactWidget(
                modifier = Modifier.fillMaxWidth(),
                user = user,
                actions = {
                    if (requestedTo.contains(user.id)) {
                        TextButton(
                            enabled = false,
                            onClick = { onAdd(user) },
                        ) {
                            Text(
                                text = stringResource(R.string.screen_add_contact_requested)
                                    .uppercase()
                            )
                        }
                    } else {
                        TextButton(onClick = { onAdd(user) }) {
                            Text(
                                text = stringResource(R.string.generic_add)
                                    .uppercase()
                            )
                        }
                    }
                }
            )
        }
    }
}
