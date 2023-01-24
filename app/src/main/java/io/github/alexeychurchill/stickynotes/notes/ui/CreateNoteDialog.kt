package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.core.Spacing.Big
import io.github.alexeychurchill.stickynotes.core.Spacing.DialogContentActions
import io.github.alexeychurchill.stickynotes.core.Spacing.Regular

@Composable
fun CreateNoteDialog(
    onProceed: (String) -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onCancel() },
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            elevation = 24.dp,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = Big),
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)) {
                    Text(
                        modifier = Modifier.align(CenterStart),
                        text = stringResource(R.string.dialog_create_note_title),
                        style = MaterialTheme.typography.h6,
                    )
                }

                var title by remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = DialogContentActions),
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(
                            text = stringResource(R.string.dialog_create_note_title_label),
                        )
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Regular),
                ) {
                    TextButton(
                        modifier = Modifier.align(CenterStart),
                        onClick = { onCancel() },
                    ) {
                        Text(
                            text = stringResource(R.string.generic_cancel).uppercase(),
                        )
                    }

                    TextButton(
                        modifier = Modifier.align(CenterEnd),
                        onClick = { onProceed(title) },
                    ) {
                        Text(
                            text = stringResource(id = R.string.dialog_create_note_action_create)
                                .uppercase(),
                        )
                    }
                }
            }
        }
    }
}
