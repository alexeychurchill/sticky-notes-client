package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.core.ui.ProgressDialog
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Big
import io.github.alexeychurchill.stickynotes.notes.ModalState
import io.github.alexeychurchill.stickynotes.notes.ModalState.*
import io.github.alexeychurchill.stickynotes.notes.NotesState
import io.github.alexeychurchill.stickynotes.notes.NotesState.*
import io.github.alexeychurchill.stickynotes.notes.NotesState.None
import io.github.alexeychurchill.stickynotes.notes.UserNotesViewModel
import kotlin.Error

@Composable
fun UserNotesList(
    viewModel: UserNotesViewModel = viewModel(),
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val notesState by viewModel.notesState.collectAsState(initial = None)
        NotesState(
            notesState = notesState,
            onReload = viewModel::reload,
        )

        FloatingActionButton(
            modifier = Modifier
                .align(BottomEnd)
                .padding(Big),
            onClick = viewModel::createNote,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
        }

        Dialogs(viewModel)
    }
}

@Composable
private fun BoxScope.NotesState(
    notesState: NotesState,
    onReload: () -> Unit,
) {
    when (notesState) {
        is Loading -> CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .align(Center),
            strokeWidth = 4.dp,
        )

        is Loaded -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            val notes = notesState.items
            items(items = notes, key = { it.id }) { note ->
                /** TODO: Call Note Entry composable **/
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                )
            }
        }

        is Error -> OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .widthIn(max = 320.dp)
                .align(Center),
            onClick = onReload,
        ) {
            Text(text = stringResource(R.string.generic_retry).uppercase())
        }
    }
}

@Composable
private fun Dialogs(viewModel: UserNotesViewModel) {
    val operation by viewModel.modalState.collectAsState(initial = ModalState.None)
    when (operation) {
        CreateNote -> CreateNoteDialog(
            onProceed = viewModel::confirmCreateNote,
            onCancel = viewModel::cancelCreateNote,
        )

        is DeleteNote -> ConfirmDeleteNoteDialog(
            noteTitle = (operation as DeleteNote).entry.title,
            onConfirm = viewModel::confirmDeleteNote,
            onDismiss = viewModel::rejectDeleteNote,
        )

        InProgress -> ProgressDialog()

        ModalState.None -> {
            /** NO OP **/
        }
    }
}
