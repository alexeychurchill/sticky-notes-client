@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.alexeychurchill.stickynotes.R

@Composable
fun CreateNoteDialog(
    onProceed: (String) -> Unit,
    onCancel: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.dialog_create_note_title))
        },
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = {
                    Text(
                        text = stringResource(R.string.dialog_create_note_title_label),
                    )
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onProceed(title) },
            ) {
                Text(
                    text = stringResource(id = R.string.generic_create).uppercase(),
                )
            }
        },
        onDismissRequest = { onCancel() },
        dismissButton = {
            TextButton(
                onClick = { onCancel() },
            ) {
                Text(
                    text = stringResource(R.string.generic_cancel).uppercase(),
                )
            }
        },
    )
}
