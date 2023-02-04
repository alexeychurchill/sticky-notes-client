package io.github.alexeychurchill.stickynotes.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.account.AuthMode.LOGIN
import io.github.alexeychurchill.stickynotes.account.AuthMode.REGISTER
import io.github.alexeychurchill.stickynotes.account.AuthState.Authorized
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val _goToAppEvent = MutableSharedFlow<Unit>()

    private val _authMode = MutableStateFlow(LOGIN)

    val goToAppEvent: Flow<Unit>
        get() = _goToAppEvent

    val authMode: Flow<AuthMode>
        get() = _authMode

    val actionText: Flow<Int>
        get() = _authMode.map { mode ->
            when (mode) {
                LOGIN -> R.string.text_button_join
                REGISTER -> R.string.text_button_back_login
            }
        }

    init {
        viewModelScope.launch {
            accountRepository.authState.collect { state ->
                if (state is Authorized) {
                    _goToAppEvent.emit(Unit)
                }
            }
        }
    }

    fun toggleMode(mode: AuthMode? = null) {
        _authMode.update {
            mode?.let { specified ->
                return@update specified
            }
            if (it == LOGIN) REGISTER else LOGIN
        }
    }
}
