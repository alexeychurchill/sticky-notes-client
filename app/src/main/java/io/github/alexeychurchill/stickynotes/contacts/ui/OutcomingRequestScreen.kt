package io.github.alexeychurchill.stickynotes.contacts.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactRequestsViewModel
import io.github.alexeychurchill.stickynotes.core.model.User
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Large
import io.github.alexeychurchill.stickynotes.core.ui.Spacing.Regular
import io.github.alexeychurchill.stickynotes.core.ui.space

@Composable
fun OutcomingRequestScreen(
    viewModel: ContactRequestsViewModel = viewModel(),
) {
    val requests by viewModel.outcomingRequests.collectAsState()
    LazyColumn {
        space(Regular)

        items(requests) { requestUser ->
            ContactWidget(
                modifier = Modifier.fillMaxWidth(),
                user = requestUser,
                actions = {
                    Actions(viewModel = viewModel, user = requestUser)
                },
            )
        }

        space(Large)
    }
}

@Composable
private fun RowScope.Actions(
    viewModel: ContactRequestsViewModel,
    user: User,
) {
    TextButton(
        modifier = Modifier.padding(end = Regular),
        onClick = { viewModel.cancelOutcomingRequest(user) },
    ) {
        val caption = stringResource(R.string.generic_cancel).uppercase()
        Text(text = caption)
    }
}
