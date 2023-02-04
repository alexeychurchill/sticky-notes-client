package io.github.alexeychurchill.stickynotes.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

fun LazyListScope.space(size: Dp) {
    item {
        Box(modifier = Modifier.height(size))
    }
}
