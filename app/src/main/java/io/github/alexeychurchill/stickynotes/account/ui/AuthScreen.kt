package io.github.alexeychurchill.stickynotes.account.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.account.AuthMode.LOGIN
import io.github.alexeychurchill.stickynotes.account.AuthMode.REGISTER
import io.github.alexeychurchill.stickynotes.account.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
) {
    val mode by viewModel.authMode.collectAsState(initial = LOGIN)
    when (mode) {
        LOGIN -> {
            LoginScreen(
                onRegisterCall = {
                    viewModel.toggleMode(REGISTER)
                },
            )
        }

        REGISTER -> {
            RegisterScreen(
                onCancelRegister = {
                    viewModel.toggleMode(LOGIN)
                },
            )
        }
    }
}
