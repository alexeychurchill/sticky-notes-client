package io.github.alexeychurchill.stickynotes.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

fun LazyListScope.space(size: Dp) {
    item {
        Box(modifier = Modifier.size(size))
    }
}

fun LazyListScope.space(size: Int) {
    item {
        val density = LocalDensity.current
        val dpSize = Dp(size / density.density)
        Box(modifier = Modifier.size(dpSize))
    }
}
