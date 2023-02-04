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
class LoginViewModel @Inject constructor(
    private val loginRepository: AccountRepository,
) : ViewModel() {

    private val _error = MutableSharedFlow<Int>()

    private val _isInProgress = MutableStateFlow(false)

    private val _onLogin = MutableSharedFlow<Unit>()

    val error: Flow<Int>
        get() = _error

    val isInProgress: Flow<Boolean>
        get() = _isInProgress

    val onLogin: Flow<Unit>
        get() = _onLogin

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.contains(" ")) {
                _error.emit(R.string.text_login_contains_space)
                return@launch
            }
            if (email.isEmpty() || password.isEmpty()) {
                _error.emit(R.string.text_login_or_password_is_empty)
                return@launch
            }
            _isInProgress.emit(true)
            try {
                val user = loginRepository.login(email, password)
                if (user != null) {
                    _onLogin.emit(Unit)
                }
            } catch (e: Throwable) {
                _error.emit(R.string.text_failure)
            } finally {
                _isInProgress.emit(false)
            }
        }
    }
}
