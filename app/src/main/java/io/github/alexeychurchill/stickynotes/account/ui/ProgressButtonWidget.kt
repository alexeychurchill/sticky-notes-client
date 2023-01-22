package io.github.alexeychurchill.stickynotes.account.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressButton(
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    isInProgress: Boolean = false,
    onClick: () -> Unit,
    buttonContent: @Composable RowScope.() -> Unit,
) {
    Box(modifier = modifier.height(height)) {
        if (isInProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(ratio = 1.0f)
                    .align(Center)
            )
        } else {
            Button(
                modifier = Modifier.fillMaxSize().align(Center),
                onClick = onClick,
            ) {
                buttonContent()
            }
        }
    }
}
