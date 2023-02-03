package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor() : ViewModel() {

    private val _onAddContactEvent = MutableSharedFlow<Unit>()

    val isLoading: StateFlow<Boolean>
        get() = MutableStateFlow(false)

    val contacts: StateFlow<List<User>>
        get() = MutableStateFlow(emptyList())

    val onAddContactEvent: Flow<Unit>
        get() = _onAddContactEvent

    fun addContact() {
        viewModelScope.launch {
            _onAddContactEvent.emit(Unit)
        }
    }

    fun deleteContact(userId: String) {
        // TODO: Implement Un-Contact
    }
}
