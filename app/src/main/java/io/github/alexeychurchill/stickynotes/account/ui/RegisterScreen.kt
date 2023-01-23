package io.github.alexeychurchill.stickynotes.account.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.account.RegisterViewModel
import io.github.alexeychurchill.stickynotes.core.Spacing.Big
import io.github.alexeychurchill.stickynotes.core.Spacing.Regular

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onCancelRegister: () -> Unit = { },
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
                text = stringResource(R.string.text_big_register),
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
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { password = it },
                label = {
                    Text(text = stringResource(R.string.login_password_title))
                },
            )

            val isInProgress by viewModel.isInProgress.collectAsState(false)
            ProgressButton(
                modifier = Modifier.fillMaxWidth(),
                isInProgress = isInProgress,
                onClick = { viewModel.register(email, password) },
            ) {
                Text(text = stringResource(R.string.text_button_register))
            }
        }

        Box(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth(),
        ) {
            TextButton(
                modifier = Modifier
                    .align(BottomCenter)
                    .padding(bottom = 56.dp),
                onClick = onCancelRegister,
            ) {
                Icon(
                    modifier = Modifier.padding(end = Regular),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                )
                Text(text = stringResource(R.string.generic_back).uppercase())
            }
        }
    }
}
