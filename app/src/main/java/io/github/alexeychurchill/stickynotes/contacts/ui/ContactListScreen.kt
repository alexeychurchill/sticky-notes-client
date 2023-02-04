@file:OptIn(ExperimentalMaterialApi::class)

package io.github.alexeychurchill.stickynotes.contacts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactListViewModel
import io.github.alexeychurchill.stickynotes.core.model.User
import io.github.alexeychurchill.stickynotes.core.ui.ShiftToReveal
import io.github.alexeychurchill.stickynotes.core.ui.ShiftValue.EndToStart
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Medium
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.space
import io.github.alexeychurchill.stickynotes.core.ui.specialColors

@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = viewModel(),
) {
    /** TODO: Add error state handling widgets **/
    Box(modifier = Modifier.fillMaxSize()) {
        val isLoading by viewModel.isLoading.collectAsState()
        val contacts by viewModel.contacts.collectAsState()

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Center))
        } else {
            LazyColumn {
                space(Regular)

                /** TODO: Remove temporary Add Contact button **/
                item {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Medium),
                        onClick = viewModel::addContact,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                        )

                        val caption = stringResource(R.string.screen_contact_list_add_contact)
                            .uppercase()
                        Text(text = caption)
                    }
                }

                items(contacts) { contact ->
                    ContactItemWidget(
                        user = contact,
                        onDelete = { viewModel.deleteContact(contact.id) },
                    )
                }

                space(96.dp)
            }
        }
    }
}

@Composable
private fun ContactItemWidget(
    user: User,
    onDelete: () -> Unit,
) {
    ShiftToReveal(
        shifts = mapOf(
            64.dp to EndToStart,
        ),
        content = {
            ContactWidget(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.surface),
                user = user,
            )
        },
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.specialColors.delete),
            ) {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(CenterEnd)
                        .aspectRatio(ratio = 1.0f)
                        .clip(RoundedCornerShape(4.dp)),
                    onClick = onDelete,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.specialColors.onDelete,
                    )
                }
            }
        }
    )
}
