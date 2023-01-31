package io.github.alexeychurchill.stickynotes.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.alexeychurchill.stickynotes.contacts.presentation.ContactRequestsViewModel
import io.github.alexeychurchill.stickynotes.contacts.ui.OutcomingRequestScreen
import io.github.alexeychurchill.stickynotes.core.ui.StickyNotesTheme

/**
 * User friend request list fragment
 */
@AndroidEntryPoint
class UserFriendRequestListFragment : Fragment() {

    private val viewModel by viewModels<ContactRequestsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val composeView = (view as? ComposeView) ?: return
        composeView.setContent {
            StickyNotesTheme {
                OutcomingRequestScreen(viewModel)
            }
        }
    }
}
