@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.alexeychurchill.stickynotes.note_editor.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.core.ui.ProgressDialog
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Medium
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.note_editor.presentation.NoteViewModel

@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = viewModel(),
) {
    LaunchedEffect(key1 = viewModel, key2 = navController) {
        viewModel.onExitEvent.collect {
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.screen_note_title))
                },
            )
        },
        floatingActionButton = {
            val isSaveEnabled by viewModel.isSaveEnabled.collectAsState()
            SaveButton(
                isEnabled = isSaveEnabled,
                onClick = viewModel::saveNote,
            )
        },
    ) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Regular),
            ) {
                val shape = MaterialTheme.shapes.extraLarge
                NoteEditWidget(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape)
                        .clipToBounds()
                        .border(
                            width = DividerDefaults.Thickness,
                            color = DividerDefaults.color,
                            shape = shape,
                        ),
                    viewModel = viewModel,
                )
            }

            val isInProgress by viewModel.inProgress.collectAsState()
            if (isInProgress) {
                ProgressDialog()
            }
        }
    }
}

@Composable
private fun SaveButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false,
    onClick: () -> Unit = { },
) {
    if (!isEnabled) {
        return
    }

    ExtendedFloatingActionButton(
        modifier = modifier,
        text = {
            Text(
                text = stringResource(R.string.screen_note_save_caption)
                    .uppercase(),
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Save,
                contentDescription = null,
            )
        },
        onClick = onClick,
    )
}

@Composable
private fun NoteEditWidget(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel,
) {
    Column(modifier = modifier) {
        val title by viewModel.title.collectAsState()
        val titleStyle = MaterialTheme.typography.titleLarge
        EditorTextField(
            maxLines = 1,
            hint = stringResource(R.string.screen_note_title_hint),
            textStyle = titleStyle,
            value = title,
            onValueChange = viewModel::onTitleChange,
        )

        Divider()

        val subject by viewModel.subject.collectAsState()
        val subjectStyle = MaterialTheme.typography.bodyLarge
            .copy(fontStyle = Italic)

        EditorTextField(
            maxLines = 1,
            hint = stringResource(R.string.screen_note_subject_hint),
            textStyle = subjectStyle,
            value = subject,
            onValueChange = viewModel::onSubjectChange,
        )

        Divider()

        val text by viewModel.text.collectAsState()
        val textStyle = MaterialTheme.typography.bodyMedium
        EditorTextField(
            modifier = Modifier.weight(weight = 1.0f),
            hint = stringResource(R.string.screen_note_text_hint),
            textStyle = textStyle,
            value = text,
            onValueChange = viewModel::onTextChange,
        )
    }
}

@Composable
private fun EditorTextField(
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    hint: String? = null,
    textStyle: TextStyle = TextStyle.Default,
    value: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = Medium,
                vertical = Medium,
            ),
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        maxLines = maxLines,
        decorationBox = { textField ->
            HintTextFieldDecoration(
                style = textStyle,
                text = hint?.takeIf { value.isEmpty() },
            ) {
                textField()
            }
        },
    )
}

@Composable
private fun HintTextFieldDecoration(
    style: TextStyle = TextStyle.Default,
    text: String? = null,
    textFieldContent: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        textFieldContent()
        val textColor = style.color.copy(alpha = 0.35f)
        val hintStyle = style.copy(color = textColor)
        if (text != null) {
            Text(
                modifier = Modifier.align(TopStart),
                text = text,
                style = hintStyle,
            )
        }
    }
}
