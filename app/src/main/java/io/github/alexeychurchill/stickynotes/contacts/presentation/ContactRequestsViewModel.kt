package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContactRequestsViewModel @Inject constructor() : ViewModel() {

    val isLoading: StateFlow<Boolean>
        get() = MutableStateFlow(false)

    val incomingRequests: StateFlow<List<User>>
        get() = MutableStateFlow(emptyList())

    val outcomingRequests: StateFlow<List<User>>
        get() = MutableStateFlow(emptyList())

    fun acceptIncoming(userId: String) {
        // TODO: Implement accepting
    }

    fun rejectIncoming(userId: String) {
        // TODO: Implement rejecting
    }

    fun cancelOutcoming(userId: String) {
        // TODO: Implement outcoming request cancellation
    }
}
