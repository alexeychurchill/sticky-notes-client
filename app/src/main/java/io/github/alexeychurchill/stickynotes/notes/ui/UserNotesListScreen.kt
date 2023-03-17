@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.app.Route
import io.github.alexeychurchill.stickynotes.core.ui.ProgressDialog
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Medium
import io.github.alexeychurchill.stickynotes.core.ui.space
import io.github.alexeychurchill.stickynotes.notes.presentation.ModalState
import io.github.alexeychurchill.stickynotes.notes.presentation.ModalState.*
import io.github.alexeychurchill.stickynotes.notes.presentation.UserNotesViewModel

@Composable
fun UserNotesListScreen(
    navController: NavController,
    viewModel: UserNotesViewModel = viewModel(),
) {
    LaunchedEffect(key1 = viewModel, key2 = navController) {
        viewModel.openNoteEvent.collect { noteId ->
            navController.navigate(Route.NoteEditor(noteId).routePath)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.screen_note_list_title))
                },
            )
        },
        floatingActionButton = {
            CreateButton(viewModel::createNote)
        },
    ) { paddings ->
        val dateTimeFormatter by viewModel.dateTimeFormatter.collectAsState()
        Box {
            WithDateTimeFormatter(dateTimeFormatter) {
                Column(modifier = Modifier.padding(top = paddings.calculateTopPadding())) {
                    val tags by viewModel.tags.collectAsState()
                    TagListWidget(
                        modifier = Modifier.fillMaxWidth(),
                        items = tags,
                        onClick = viewModel::toggleTag,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0f)
                            .padding(horizontal = Medium),
                    ) {
                        val notes by viewModel.noteEntries.collectAsState()
                        val notesListState = rememberLazyListState()

                        /** TODO: Figure how out to scroll to begin only on: **/
                        /** 1. Selected tag set change **/
                        /** 2. Adding new item **/
                        /** Actually, some kind of such, but modified code can be used: **/
                        /** LaunchedEffect(key1 = notes.size) {
                            notesListState.animateScrollToItem(index = 0)
                        } **/

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(NoteEntryShape),
                            state = notesListState,
                        ) {
                            items(notes, key = { it.entry.id }) { listItem ->
                                NoteEntryListItem(
                                    modifier = Modifier
                                        .padding(bottom = Medium)
                                        .animateItemPlacement(),
                                    noteEntry = listItem.entry,
                                    isPinned = listItem.isPinned,
                                    onEntryAction = viewModel::handleNoteAction,
                                    onEntryClick = { viewModel.openNote(it.id) },
                                )
                            }

                            space(paddings.calculateBottomPadding() + 96.dp)
                        }
                    }
                }
            }
            Dialogs(viewModel)
        }
    }
}

@Composable
private fun CreateButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Rounded.NoteAdd,
                contentDescription = null,
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.generic_create).uppercase()
            )
        },
    )
}

@Composable
private fun Dialogs(viewModel: UserNotesViewModel) {
    val operation by viewModel.modalState.collectAsState(
        initial = ModalState.None
    )
    when (operation) {
        is CreateNote -> CreateNoteDialog(
            onProceed = viewModel::confirmCreateNote,
            onCancel = viewModel::cancelCreateNote,
        )

        is DeleteNote -> ConfirmDeleteNoteDialog(
            noteTitle = (operation as? DeleteNote)?.entry?.title ?: "",
            onConfirm = {
                (operation as? DeleteNote)
                    ?.entry
                    ?.id
                    ?.let(viewModel::proceedDeleteNote)
            },
            onDismiss = viewModel::rejectDeleteNote,
        )

        is InProgress -> ProgressDialog()

        is ModalState.None -> { /** NO OP **/ }
    }

    val cannotPinDueToLimitId by viewModel.cannotPinDueToLimitId.collectAsState()
    cannotPinDueToLimitId?.let { id ->
        PinLimitStateDialog(
            onProceed = { viewModel.proceedPinOverLimit(id) },
            onDismiss = { viewModel.cancelPinOverLimit() },
        )
    }
}
