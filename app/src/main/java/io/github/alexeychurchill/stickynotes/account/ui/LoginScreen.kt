package io.github.alexeychurchill.stickynotes.account.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.account.LoginViewModel
import io.github.alexeychurchill.stickynotes.core.Spacing.Big
import io.github.alexeychurchill.stickynotes.core.Spacing.Regular

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1.0f))

        Column(
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth(fraction = 0.75f),
        ) {
            Text(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(bottom = Big),
                text = stringResource(R.string.text_big_login),
                style = MaterialTheme.typography.h4,
            )

            var email by remember { mutableStateOf("") }
            TextField(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .fillMaxWidth()
                    .padding(bottom = Regular),
                value = email,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = Email,
                    imeAction = Next,
                    autoCorrect = false,
                ),
                onValueChange = { email = it },
                label = {
                    Text(text = stringResource(R.string.login_email_title))
                },
            )

            var password by remember { mutableStateOf("") }
            TextField(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .fillMaxWidth()
                    .padding(bottom = Regular),
                value = password,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = Password,
                    imeAction = Done,
                    autoCorrect = false,
                ),
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    Text(text = stringResource(id = R.string.login_password_title))
                },
            )

            val isInProgress by viewModel.isInProgress.collectAsState(false)
            ProgressButton(
                modifier = Modifier.fillMaxWidth(),
                isInProgress = isInProgress,
                onClick = { viewModel.login(email, password) },
            ) {
                Text(text = stringResource(R.string.text_button_login))
            }
        }

        Box(modifier = Modifier.weight(1.0f))
    }
}
