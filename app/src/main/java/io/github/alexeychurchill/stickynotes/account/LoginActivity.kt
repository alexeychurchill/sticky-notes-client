package io.github.alexeychurchill.stickynotes.account

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.R
import io.github.alexeychurchill.stickynotes.account.AuthMode.LOGIN
import io.github.alexeychurchill.stickynotes.account.AuthMode.REGISTER
import io.github.alexeychurchill.stickynotes.activity.MainActivity

/**
 * Login and register activity
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initModeHandler()
        initActionButtonText()
        initGoToAppEventHandler()

        // Swap fragments button
        findViewById<LinearLayout>(R.id.llButtonSwapAction).setOnClickListener {
            viewModel.toggleMode()
        }
    }

    private fun initModeHandler() {
        lifecycleScope.launchWhenCreated {
            viewModel.authMode.collect { mode ->
                val fragment = when(mode) {
                    LOGIN -> LoginFragment()
                    REGISTER -> RegisterFragment()
                }
                switchFragment(fragment)
            }
        }
    }

    private fun initActionButtonText() {
        lifecycleScope.launchWhenCreated {
            viewModel.actionText.collect { actionTextResId ->
                findViewById<TextView>(R.id.tvButtonActionTitle).setText(actionTextResId)
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

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}
