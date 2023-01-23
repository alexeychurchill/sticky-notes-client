package io.github.alexeychurchill.stickynotes.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.account.ui.AuthScreen
import io.github.alexeychurchill.stickynotes.activity.MainActivity
import io.github.alexeychurchill.stickynotes.core.StickyNotesTheme

/**
 * Login and register activity
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGoToAppEventHandler()
        setContent {
            StickyNotesTheme {
                AuthScreen(viewModel)
            }
        }
    }

    private fun initGoToAppEventHandler() {
        lifecycleScope.launchWhenCreated {
            viewModel.goToAppEvent.collect {
                val mainActivityIntent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            }
        }
    }
}
