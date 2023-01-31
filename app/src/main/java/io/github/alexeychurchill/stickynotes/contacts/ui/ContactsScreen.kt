package io.github.alexeychurchill.stickynotes.contacts.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactListViewModel
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactRequestsViewModel
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactScreenMode
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactsScreenViewModel

@Composable
fun ContactsScreen(
    viewModel: ContactsScreenViewModel = viewModel(),
) {
    Scaffold { paddings ->
        Column(modifier = Modifier.padding(paddings)) {
            val tabsState by viewModel.state.collectAsState()
            ContactsTabs(
                modifier = Modifier.fillMaxWidth(),
                state = tabsState,
                onSelectMode = viewModel::onSelectMode,
            )

            val mode by viewModel.mode.collectAsState()
            val listViewModel = hiltViewModel<ContactListViewModel>()
            val requestsViewModel = hiltViewModel<ContactRequestsViewModel>()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1.0f),
            ) {

                when (mode) {
                    ContactScreenMode.CONTACTS -> {
                        ContactListScreen(listViewModel)
                    }
                    ContactScreenMode.REQUESTS -> {
                        ContactRequestsScreen(requestsViewModel)
                    }
                }
            }
        }
    }
}
