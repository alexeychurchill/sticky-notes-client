package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.space
import io.github.alexeychurchill.stickynotes.notes.presentation.TagState

@Composable
fun TagListWidget(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { },
    items: List<TagState>,
) {
    LazyRow(
        modifier = modifier,
    ) {
        space(Regular)

        items(items, key = TagState::name) { tag ->
            TagDefaultListItem(
                tag = tag,
                onClick = onClick,
            )
        }

        space(Regular)
    }
}
