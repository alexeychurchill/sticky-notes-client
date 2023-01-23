package io.github.alexeychurchill.stickynotes.account.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.account.LoginViewModel
import io.github.alexeychurchill.stickynotes.core.Spacing.Big
import io.github.alexeychurchill.stickynotes.core.Spacing.Regular

private const val ANNOTATION_NAV = "nav"
private const val TAG_SIGNUP = "signup"

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onRegisterCall: () -> Unit = { },
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

        Box(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth(),
        ) {
            SignUpCtaWidget(
                modifier = Modifier
                    .align(TopCenter)
                    .padding(top = 56.dp),
                onSignUpClick = onRegisterCall,
            )
        }
    }
}

@Composable
private fun SignUpCtaWidget(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit
) {
    val inviteText = signUpCtaText()
    ClickableText(
        modifier = modifier,
        text = inviteText,
        style = MaterialTheme.typography.subtitle2,
    ) { charIndex ->
        val tags = inviteText.getStringAnnotations(charIndex, charIndex)
        if (tags.find { it.tag == TAG_SIGNUP } != null) {
            onSignUpClick()
        }
    }
}

@Composable
private fun signUpCtaText(): AnnotatedString = buildAnnotatedString {
    append(stringResource(R.string.login_create_account_do_not_have))
    append(' ')
    pushStringAnnotation(TAG_SIGNUP, ANNOTATION_NAV)
    pushStyle(linkSpanStyle())
    append(stringResource(R.string.login_create_account_create))
    pop()
    pop()
}

@Composable
private fun linkSpanStyle(): SpanStyle = SpanStyle(
    color = MaterialTheme.colors.secondary,
    fontWeight = Bold,
)
