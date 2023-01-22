package io.github.alexeychurchill.stickynotes.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.account.ui.LoginScreen
import io.github.alexeychurchill.stickynotes.core.StickyNotesTheme

/**
 * Login fragment
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {
    interface OnLoggedInListener {
        fun onLoggedIn()
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.onLogin.collect {
                (requireActivity() as? OnLoggedInListener)?.onLoggedIn()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.error.collect {
                Toast.makeText(requireContext(), it, LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view as? ComposeView ?: return

        composeView.setContent {
            StickyNotesTheme {
                LoginScreen(viewModel)
            }
        }
    }
}
