@file:OptIn(ExperimentalFoundationApi::class)

package io.github.alexeychurchill.stickynotes.contacts.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactRequestsViewModel
import io.github.alexeychurchill.stickynotes.core.model.User
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Medium
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Small
import io.github.alexeychurchill.stickynotes.core.ui.space

@Composable
fun ContactRequestsScreen(
    viewModel: ContactRequestsViewModel = viewModel(),
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val incomingRequests by viewModel.incomingRequests.collectAsState()
    val outcomingRequests by viewModel.outcomingRequests.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Center),
            )

            incomingRequests.isEmpty() && outcomingRequests.isEmpty() -> Text(
                modifier = Modifier
                    .align(Center)
                    .fillMaxWidth(fraction = 0.65f),
                text = stringResource(R.string.screen_requests_empty),
                style = MaterialTheme.typography.body1,
            )

            else -> LazyColumn {
                incomingSection(viewModel, incomingRequests)
                outcomingSection(viewModel, outcomingRequests)
                space(96.dp)
            }
        }
    }


}

private fun LazyListScope.incomingSection(
    viewModel: ContactRequestsViewModel,
    incomingRequests: List<User>,
) {
    if (incomingRequests.isEmpty()) {
        return
    }

    stickyHeader {
        SectionHeader(
            text = stringResource(id = R.string.screen_requests_incoming_section),
        )
    }

    items(incomingRequests) { requestUser ->
        ContactWidget(
            modifier = Modifier.fillMaxWidth(),
            user = requestUser,
            actions = {
                IncomingActions(
                    viewModel = viewModel,
                    user = requestUser,
                )
            },
        )
    }
}

private fun LazyListScope.outcomingSection(
    viewModel: ContactRequestsViewModel,
    outcomingRequests: List<User>,
) {
    if (outcomingRequests.isEmpty()) {
        return
    }

    stickyHeader {
        SectionHeader(
            text = stringResource(R.string.screen_requests_outcoming_section),
        )
    }

    items(outcomingRequests) { requestUser ->
        ContactWidget(
            modifier = Modifier.fillMaxWidth(),
            user = requestUser,
            actions = {
                OutcomingActions(
                    viewModel = viewModel,
                    user = requestUser,
                )
            },
        )
    }
}

@Composable
private fun SectionHeader(
    text: String
) {
    val rootColor = MaterialTheme.colors.primary
    val surfaceColor = MaterialTheme.colors.surface
    val backgroundColor = rootColor.copy(alpha = 0.15f)
        .compositeOver(surfaceColor)

    Column(modifier = Modifier.background(color = backgroundColor)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Regular)
                .padding(
                    top = Medium,
                    bottom = Small,
                ),
        ) {
            Text(
                text = text,
                maxLines = 1,
                overflow = Ellipsis,
                style = MaterialTheme.typography.body1,
                fontWeight = Bold,
            )
        }

        Divider(
            color = rootColor.copy(alpha = 0.25f),
        )
    }
}

@Composable
private fun RowScope.IncomingActions(
    viewModel: ContactRequestsViewModel,
    user: User,
) {
    TextButton(
        modifier = Modifier.padding(end = Regular),
        onClick = { viewModel.rejectIncoming(user.id) },
    ) {
        val caption = stringResource(R.string.generic_reject).uppercase()
        Text(text = caption)
    }
    
    TextButton(
        modifier = Modifier.padding(end = Regular),
        onClick = { viewModel.acceptIncoming(user.id) },
    ) {
        val caption = stringResource(R.string.generic_accept).uppercase()
        Text(text = caption)
    }
}

@Composable
private fun RowScope.OutcomingActions(
    viewModel: ContactRequestsViewModel,
    user: User,
) {
    TextButton(
        modifier = Modifier.padding(end = Regular),
        onClick = { viewModel.cancelOutcoming(user.id) },
    ) {
        val caption = stringResource(R.string.generic_cancel).uppercase()
        Text(text = caption)
    }
}
