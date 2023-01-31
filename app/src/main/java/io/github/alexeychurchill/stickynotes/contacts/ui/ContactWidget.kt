package io.github.alexeychurchill.stickynotes.contacts.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.alexeychurchill.stickynotes.core.model.User
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme

@Composable
fun ContactWidget(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = { },
    user: User,
) {
    Box(
        modifier = modifier
            .height(64.dp)
            .padding(Regular)
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .align(Center)
                .clickable(
                    enabled = onClick != null,
                    onClick = onClick ?: { },
                ),
        ) {
            ContactImageWidget()

            Column(
                modifier = Modifier
                    .padding(start = Regular)
                    .align(CenterVertically),
            ) {
                val hasName = user.firstName.isNotBlank() && user.lastName.isNotBlank()
                if (hasName) {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = ExtraBold,
                    )
                }

                val loginStyle = if (hasName) {
                    MaterialTheme.typography.subtitle2
                } else {
                    MaterialTheme.typography.subtitle1
                }
                Text(
                    text = user.login,
                    style = loginStyle,
                    fontWeight = if (hasName) Normal else ExtraBold,
                )
            }
        }

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .align(CenterEnd),
            horizontalArrangement = Arrangement.End,
            CenterVertically,
            content = actions,
        )
    }
}

@Composable
private fun ContactImageWidget(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val imageRootColor = MaterialTheme.colors.primary
        val imageBgColor = imageRootColor
            .copy(alpha = 0.35f)
            .compositeOver(MaterialTheme.colors.surface)

        val imageFgColor = imageRootColor
            .copy(alpha = 0.85f)
            .compositeOver(MaterialTheme.colors.surface)

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(ratio = 1.0f)
                .clip(CircleShape)
                .background(color = imageBgColor),
        ) {
            val icon = Icons.Rounded.Person
            Image(
                modifier = Modifier
                    .matchParentSize()
                    .padding(Regular),
                imageVector = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = imageFgColor,
                ),
            )
        }
    }
}

@Preview(widthDp = 360)
@Composable
fun ContactWidget_Preview() {
    StickyNotesTheme {
        Surface {
            ContactWidget(
                user = User(
                    id = "abc123",
                    login = "test_login",
                    firstName = "Test",
                    lastName = "Login",
                ),
                actions = {
                    TextButton(onClick = { /** Ignore **/ }) {
                        Text(text = "Test 1".uppercase())
                    }

                    TextButton(onClick = { /** Ignore **/ }) {
                        Text(text = "Test 2".uppercase())
                    }
                },
            )
        }
    }
}
