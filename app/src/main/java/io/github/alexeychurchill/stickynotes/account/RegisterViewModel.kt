package io.github.alexeychurchill.stickynotes.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val _error = MutableSharedFlow<Int>()

    private val _isInProgress = MutableStateFlow(false)

    private val _onRegistered = MutableSharedFlow<Unit>()

    val error: Flow<Int>
        get() = _error

    val isInProgress: Flow<Boolean>
        get() = _isInProgress

    val onRegistered: Flow<Unit>
        get() = _onRegistered

    fun register(login: String, password: String) {
        viewModelScope.launch {
            if (login.contains(" ")) {
                _error.emit(R.string.text_login_contains_space)
                return@launch
            }
            if (login.isEmpty() || password.isEmpty()) {
                _error.emit(R.string.text_login_or_password_is_empty)
                return@launch
            }

            _isInProgress.emit(true)
            try {
                val user = accountRepository.register(login, password)
                if (user != null) {
                    _onRegistered.emit(Unit)
                }
            } catch (e: Throwable) {
                _error.emit(R.string.text_failure)
            } finally {
                _isInProgress.emit(false)
            }
        }
    }
}
