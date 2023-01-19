package io.github.alexeychurchill.stickynotes.auth

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
    private val loginRepository: LoginRepository,
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

    fun login(login: String, password: String) {
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
                val user = loginRepository.login(login, password)
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
