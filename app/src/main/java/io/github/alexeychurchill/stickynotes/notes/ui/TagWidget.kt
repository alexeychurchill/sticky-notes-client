@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package io.github.alexeychurchill.stickynotes.notes.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.notes.presentation.TagState

@Composable
fun TagWidget(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { },
    tag: TagState,
) {
    FilterChip(
        modifier = modifier,
        selected = tag.selected,
        onClick = { onClick(tag.name) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Tag,
                contentDescription = null,
            )
        },
        label = {
            Text(text = tag.name)
        },
    )
}

@Composable
fun LazyItemScope.TagDefaultListItem(
    onClick: (String) -> Unit = { },
    tag: TagState,
) {
    TagWidget(
        modifier = Modifier.padding(horizontal = Regular).animateItemPlacement(),
        onClick = onClick,
        tag = tag,
    )
}
