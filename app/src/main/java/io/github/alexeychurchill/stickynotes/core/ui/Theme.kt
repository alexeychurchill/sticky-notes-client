package io.github.alexeychurchill.stickynotes.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

@Composable
fun StickyNotesTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = StickyNotesColorsLight,
    ) {
        CompositionLocalProvider(
            LocalSpecialColors provides SpecialColorsLight
        ) {
            content()
        }
    }
}

val MaterialTheme.specialColors: SpecialColors
    @Composable
    @ReadOnlyComposable
    get() = LocalSpecialColors.current

data class SpecialColors(
    val delete: Color,
    val onDelete: Color,
)

private val LocalSpecialColors = compositionLocalOf { SpecialColorsLight }

private val StickyNotesColorsLight = Colors(
    primary = Color(0xff607d8b),
    primaryVariant = Color(0xff34515e),
    secondary = Color(0xff4caf50),
    secondaryVariant = Color(0xff087f23),
    background = Color.White,
    surface = Color.White,
    error = Color(0xffb00020),
    onPrimary = Color(0xffeeeeee),
    onSecondary = Color(0xfff5f5f5),
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
    isLight = true,
)

private val SpecialColorsLight = SpecialColors(
    delete = Color(0xffe53935),
    onDelete = Color.White,
)

// TODO: Add dark colors
