package io.github.alexeychurchill.stickynotes.contacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.account.domain.UserRepository
import io.github.alexeychurchill.stickynotes.core.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<User>?>(null)

    private val _onAddContactEvent = MutableSharedFlow<Unit>()

    val isLoading: StateFlow<Boolean> by lazy {
        _contacts
            .map { it == null }
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = true,
            )
    }

    val contacts: StateFlow<List<User>> by lazy {
        _contacts
            .filterIsInstance<List<User>>()
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = emptyList(),
            )
    }

    val onAddContactEvent: Flow<Unit>
        get() = _onAddContactEvent

    init {
        viewModelScope.launch {
            _contacts.emit(null)
            userRepository.contacts().collect { users ->
                _contacts.emit(users)
            }
        }
    }

    fun addContact() {
        viewModelScope.launch {
            _onAddContactEvent.emit(Unit)
        }
    }

    fun deleteContact(userId: String) {
        // TODO: Implement Un-Contact
    }
}
