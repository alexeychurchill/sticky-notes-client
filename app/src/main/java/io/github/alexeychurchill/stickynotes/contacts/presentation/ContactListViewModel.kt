package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor() : ViewModel() {

    val isLoading: StateFlow<Boolean>
        get() = MutableStateFlow(false)

    val contacts: StateFlow<List<User>>
        get() = MutableStateFlow(emptyList())

    fun addContact() {
        // TODO: Start Add Contact flow
    }

    fun deleteContact(userId: String) {
        // TODO: Implement Un-Contact
    }
}
