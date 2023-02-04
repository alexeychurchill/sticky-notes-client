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
class ContactRequestsViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _outgoingRequests = MutableStateFlow<List<User>?>(null)

    private val _ingoingRequests = MutableStateFlow<List<User>?>(null)

    val isLoading: StateFlow<Boolean> by lazy {
        combine(
            _outgoingRequests,
            _ingoingRequests,
        ) { outgoing, ingoing ->
            outgoing == null || ingoing == null
        }
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = true,
            )
    }

    val incomingRequests: StateFlow<List<User>>
        get() = _ingoingRequests
            .filterIsInstance<List<User>>()
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = emptyList(),
            )

    val outcomingRequests: StateFlow<List<User>>
        get() = _outgoingRequests
            .filterIsInstance<List<User>>()
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = emptyList(),
            )

    init {
        viewModelScope.launch {
            _outgoingRequests.emit(null)
            userRepository.outgoingRequests().collect { users ->
                _outgoingRequests.emit(users)
            }
        }

        viewModelScope.launch {
            _ingoingRequests.emit(null)
            userRepository.ingoingRequests().collect { users ->
                _ingoingRequests.emit(users)
            }
        }
    }

    fun acceptIncoming(userId: String) {
        viewModelScope.launch {
            userRepository.acceptRequest(userId)
        }
    }

    fun rejectIncoming(userId: String) {
        viewModelScope.launch {
            userRepository.rejectRequest(userId)
        }
    }

    fun cancelOutcoming(userId: String) {
        viewModelScope.launch {
            userRepository.cancelRequest(userId)
        }
    }
}
